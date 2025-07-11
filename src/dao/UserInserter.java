package src.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * A simple CLI tool to insert a user into the database via user input.
 */
public class UserInserter {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/tracker";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Fh_*27112011";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        insertUser(username, password);
    }

    /**
     * Inserts a new user into the "users" table.
     */
    private static void insertUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User successfully added!");
            }

        } catch (SQLException e) {
            System.err.println("Error while adding user: " + e.getMessage());
        }
    }
}
