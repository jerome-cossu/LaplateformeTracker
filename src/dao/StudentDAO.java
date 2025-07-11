package src.dao;

import src.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Data Access Object (DAO) class for performing database operations on Student entities.
 */
public class StudentDAO {

    /**
     * Inserts a new student into the database.
     */
    public static void insertStudent(String firstName, String lastName, int age, int grade) {
        String sql = "INSERT INTO student(first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);
            stmt.setInt(4, grade);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while inserting: " + e.getMessage());
        }
    }

    /**
     * Retrieves all students from the database.
     */
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getInt("grade")
                );
                students.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Retrieves the last inserted student (based on descending ID order).
     */
    public static Student getLastInsertedStudent() {
        String sql = "SELECT * FROM student ORDER BY id DESC LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getInt("grade")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates an existing student record in the database.
     */
    public static void updateStudent(Student student) {
        String sql = "UPDATE student SET first_name = ?, last_name = ?, age = ?, grade = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setInt(3, student.getAge());
            pstmt.setInt(4, student.getGrade());
            pstmt.setInt(5, student.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a student from the database.
     */
    public static boolean deleteStudent(Student student) {
        String sql = "DELETE FROM student WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, student.getId());
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Searches students by a search term (matches on ID, first/last name, age, or grade).
     */
    public ObservableList<Student> searchStudents(String searchTerm) {
        ObservableList<Student> students = FXCollections.observableArrayList();

        String sql = "SELECT * FROM student WHERE " +
                     "CAST(id AS TEXT) LIKE ? OR " +
                     "LOWER(first_name) LIKE ? OR " +
                     "LOWER(last_name) LIKE ? OR " +
                     "CAST(age AS TEXT) LIKE ? OR " +
                     "CAST(grade AS TEXT) LIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + searchTerm.toLowerCase() + "%";
            System.out.println("LIKE query used: " + likeTerm);

            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, likeTerm);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getInt("grade")
                );
                students.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
}
