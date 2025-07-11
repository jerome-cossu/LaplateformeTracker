package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    // Database connection parameters
    private final String url = "jdbc:postgresql://localhost:5432/tracker";
    private final String user = "postgres";
    private final String password = "Fh_*27112011";

    /**
     * Handles the login action when the login button is clicked.
     * Checks if username and password fields are filled,
     * then validates credentials against the database.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String pass = passwordField.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Credentials are correct, load the home page
                errorLabel.setText("");
                goToHomePage();
            } else {
                errorLabel.setText("Incorrect username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Database connection error.");
        }
    }

    /**
     * Handles the registration action when the register button is clicked.
     * Checks for existing username and inserts new user into the database if valid.
     */
    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String pass = passwordField.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Check if username already exists
            String checkSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                errorLabel.setText("Username is already taken.");
                return;
            }

            // Insert new user into database
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, pass);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Account successfully created! You can now log in.");
            } else {
                errorLabel.setText("Error occurred while creating account.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Database connection error.");
        }
    }

    /**
     * Loads the home page UI after successful login.
     */
    @FXML
    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home_page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 600));
            stage.setTitle("La Plateforme Tracker - Home");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error loading the home page.");
        }
    }
}
