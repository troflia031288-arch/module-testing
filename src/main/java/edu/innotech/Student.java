package edu.innotech;

import java.util.Arrays;

public class Student {
    private int id;
    private String name;
    private int[] marks;

    public Student() {}

    public Student(int id, String name, int[] marks) {
        this.id = id;
        this.name = name;
        this.marks = marks != null ? Arrays.copyOf(marks, marks.length) : new int[0]; // Защита от null
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getMarks() {
        return Arrays.copyOf(marks, marks.length);
    }

    public void setMarks(int[] marks) {
        this.marks = marks != null ? Arrays.copyOf(marks, marks.length) : new int[0];
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name +  ", marks=" + Arrays.toString(marks) +
                "}";
    }

}