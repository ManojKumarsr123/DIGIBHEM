package com.gradebook.system;

import java.util.ArrayList;
import java.util.List;

public class GradebookManager {
    private List<Student> students;

    public GradebookManager() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean removeStudent(String studentId) {
        return students.removeIf(s -> s.getId().equals(studentId));
    }

    public Student getStudent(String studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
}