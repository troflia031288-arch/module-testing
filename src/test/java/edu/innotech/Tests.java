package edu.innotech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Ivan Ivanov");
    }

    @Test
    void testAddGrade() {
        student.addGrade(3);
        student.addGrade(4);

        List<Integer> grades = student.getGrades();

        assertEquals(2, grades.size());
        assertTrue(grades.contains(3));
        assertTrue(grades.contains(4));
    }

    @Test
    void testAddWrongGrade() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.addGrade(8);
        });

        assertEquals("8 is wrong grade", exception.getMessage());
    }

    @Test
    void testGetGradesUnchangable() {
        List<Integer> grades = student.getGrades();

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            grades.add(5);
        });

        assertNotNull(exception);
    }

    @Test
    void testEqualsAndHashCode() {
        Student anotherStudent = new Student("Ivan Ivanov");

        assertEquals(student, anotherStudent);
        assertEquals(student.hashCode(), anotherStudent.hashCode());

        anotherStudent.addGrade(3);

        assertNotEquals(student, anotherStudent);

        student.addGrade(3);

        assertEquals(student, anotherStudent);
    }

    @Test
    void testToString() {
        student.addGrade(5);

        String expectedString = "Student{name=Ivan Ivanov, marks=[5]}";
        assertEquals(expectedString, student.toString());
    }
}
