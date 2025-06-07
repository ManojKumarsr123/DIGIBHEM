package com.gradebook.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Map;

public class GradebookGUI {
    private GradebookManager manager;
    private JFrame frame;
    private JTextField idField, nameField, subjectField, gradeField;
    private JTextArea outputArea;
    private JComboBox<String> studentComboBox;

    public GradebookGUI() {
        manager = new GradebookManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Student Gradebook System");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel outputPanel = createOutputPanel();

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(outputPanel, BorderLayout.SOUTH);

        frame.add(contentPanel);
        centerFrameOnScreen();
        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)), 
            "Student Information", 
            javax.swing.border.TitledBorder.LEFT, 
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(70, 130, 180)));
        panel.setBackground(Color.WHITE);

        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        panel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        idField.setFont(inputFont);
        panel.add(idField);

        panel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        nameField.setFont(inputFont);
        panel.add(nameField);

        panel.add(new JLabel("Subject:"));
        subjectField = new JTextField();
        subjectField.setFont(inputFont);
        panel.add(subjectField);

        panel.add(new JLabel("Grade (0-100):"));
        gradeField = new JTextField();
        gradeField.setFont(inputFont);
        panel.add(gradeField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(240, 240, 240));

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Dimension buttonSize = new Dimension(180, 40);

        panel.add(createStyledButton("Add Student", new Color(70, 130, 180), Color.BLUE, buttonFont, buttonSize, e -> addStudent()));
        panel.add(createStyledButton("Add Grade", new Color(70, 130, 180), Color.BLUE, buttonFont, buttonSize, e -> addGrade()));
        panel.add(createStyledButton("View Grades", new Color(60, 179, 113), Color.BLUE, buttonFont, buttonSize, e -> viewGrades()));
        panel.add(createStyledButton("Remove Student", new Color(220, 20, 60), Color.BLUE, buttonFont, buttonSize, e -> removeStudent()));
        panel.add(createStyledButton("Generate Report", new Color(138, 43, 226), Color.BLUE, buttonFont, buttonSize, e -> generateReport()));

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor, Font font, Dimension size, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.addActionListener(action);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)), 
            "Output", 
            javax.swing.border.TitledBorder.LEFT, 
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(70, 130, 180)));
        panel.setBackground(Color.WHITE);

        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(new Color(248, 248, 248));
        outputArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        studentComboBox = new JComboBox<>();
        studentComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        updateStudentComboBox();
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        comboPanel.add(new JLabel("Select Student:"));
        comboPanel.add(studentComboBox);

        panel.add(comboPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void centerFrameOnScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2);
    }

    private void updateStudentComboBox() {
        studentComboBox.removeAllItems();
        for (Student student : manager.getAllStudents()) {
            studentComboBox.addItem(student.getId() + " - " + student.getName());
        }
    }

    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showError("ID and Name cannot be empty!");
            return;
        }

        if (manager.getStudent(id) != null) {
            showError("Student with this ID already exists!");
            return;
        }

        manager.addStudent(new Student(id, name));
        updateStudentComboBox();
        outputArea.append("Added student: " + name + " (ID: " + id + ")\n");
        clearInputFields();
    }

    private void addGrade() {
        String selected = (String) studentComboBox.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            showError("No student selected!");
            return;
        }

        String studentId = selected.split(" - ")[0];
        String subject = subjectField.getText().trim();
        String gradeStr = gradeField.getText().trim();

        if (subject.isEmpty() || gradeStr.isEmpty()) {
            showError("Subject and Grade cannot be empty!");
            return;
        }

        try {
            double grade = Double.parseDouble(gradeStr);
            if (grade < 0 || grade > 100) {
                showError("Grade must be between 0 and 100!");
                return;
            }

            Student student = manager.getStudent(studentId);
            if (student != null) {
                student.addGrade(subject, grade);
                outputArea.append("Added grade for " + student.getName() + ": " + subject + " = " + grade + "\n");
                clearInputFields();
            }
        } catch (NumberFormatException e) {
            showError("Invalid grade format!");
        }
    }

    private void viewGrades() {
        String selected = (String) studentComboBox.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            showError("No student selected!");
            return;
        }

        String studentId = selected.split(" - ")[0];
        Student student = manager.getStudent(studentId);
        
        if (student != null) {
            outputArea.append("\nGrades for " + student.getName() + " (ID: " + studentId + "):\n");
            for (Map.Entry<String, Double> entry : student.getGrades().entrySet()) {
                outputArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            outputArea.append("Average: " + student.calculateAverage() + "\n");
        }
    }

    private void removeStudent() {
        String selected = (String) studentComboBox.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            showError("No student selected!");
            return;
        }

        String studentId = selected.split(" - ")[0];
        if (manager.removeStudent(studentId)) {
            outputArea.append("Removed student with ID: " + studentId + "\n");
            updateStudentComboBox();
        } else {
            showError("Failed to remove student!");
        }
    }

    private void generateReport() {
        String selected = (String) studentComboBox.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            showError("No student selected!");
            return;
        }

        String studentId = selected.split(" - ")[0];
        Student student = manager.getStudent(studentId);
        
        if (student != null) {
            StringBuilder report = new StringBuilder();
            report.append("\n=== GRADE REPORT ===\n");
            report.append("Student: ").append(student.getName()).append("\n");
            report.append("ID: ").append(student.getId()).append("\n\n");
            report.append("SUBJECTS AND GRADES:\n");
            
            for (Map.Entry<String, Double> entry : student.getGrades().entrySet()) {
                report.append(String.format("%-15s: %5.2f\n", entry.getKey(), entry.getValue()));
            }
            
            report.append("\nAVERAGE GRADE: ").append(String.format("%.2f", student.calculateAverage())).append("\n");
            report.append("========================\n");
            
            outputArea.append(report.toString());
            
            int option = JOptionPane.showConfirmDialog(frame, "Save report to file?", "Save Report", JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        Files.write(fileChooser.getSelectedFile().toPath(), report.toString().getBytes());
                        outputArea.append("Report saved to: " + fileChooser.getSelectedFile() + "\n");
                    } catch (IOException e) {
                        showError("Error saving file: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        subjectField.setText("");
        gradeField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradebookGUI());
    }
}