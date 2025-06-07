package com.gradebook.system;

import java.util.Map;
import java.util.HashMap;

public class Student {
    private String id;
    private String name;
    private Map<String, Double> grades;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new HashMap<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Map<String, Double> getGrades() { return grades; }

    public void addGrade(String subject, double grade) {
        grades.put(subject, grade);
    }

    public void removeGrade(String subject) {
        grades.remove(subject);
    }

    public double calculateAverage() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0.0;
        for (double grade : grades.values()) {
            sum += grade;
        }
        return sum / grades.size();
    }
}