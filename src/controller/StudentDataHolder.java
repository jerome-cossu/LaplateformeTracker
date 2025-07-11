package src.controller;

import src.model.Student;
import java.util.List;

/**
 * This class holds a static list of students
 * to share data between different controllers or views.
 */
public class StudentDataHolder {
    // Static list of students shared across the application
    private static List<Student> students;

    /**
     * Sets the list of students.
     * @param list the list of students to store
     */
    public static void setStudents(List<Student> list) {
        students = list;
    }

    /**
     * Gets the stored list of students.
     * @return the list of students
     */
    public static List<Student> getStudents() {
        return students;
    }
}
