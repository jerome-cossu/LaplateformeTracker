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

    private final String url = "jdbc:postgresql://localhost:5432/tracker";
    private final String user = "postgres";
    private final String password = "Fh_*27112011";

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String pass = passwordField.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Identifiants corrects, on charge la page home
                errorLabel.setText("");
                goToHomePage();
            } else {
                errorLabel.setText("Identifiant ou mot de passe incorrect.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur de connexion à la base de données.");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String pass = passwordField.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Vérifier si l'utilisateur existe déjà
            String checkSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                errorLabel.setText("Nom d'utilisateur déjà utilisé.");
                return;
            }

            // Insérer le nouvel utilisateur
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, pass);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Compte créé avec succès ! Vous pouvez vous connecter.");
            } else {
                errorLabel.setText("Erreur lors de la création du compte.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur de connexion à la base de données.");
        }
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home_page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow(); // récupère la fenêtre actuelle
            stage.setScene(new Scene(root, 1200, 600));
            stage.setTitle("La Plateforme Tracker - Home");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors du chargement de la page d'accueil.");
        }
    }
}
