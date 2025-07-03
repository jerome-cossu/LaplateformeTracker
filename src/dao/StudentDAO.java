package src.dao;

import src.model.Student;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StudentDAO {
    public static void insertStudent(String first_name, String last_name, int age, int grade) {
        String sql = "INSERT INTO student(first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, first_name);
            stmt.setString(2, last_name);
            stmt.setInt(3, age);
            stmt.setInt(4, grade);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

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

    public static Student getLastInsertedStudent() {
        String sql = "SELECT * FROM student ORDER BY id DESC LIMIT 1";
        try (Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                Student lastStudent = new Student(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getInt("grade")
                );
                return lastStudent;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}