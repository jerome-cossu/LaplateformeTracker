package models;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginPage {
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginPage() {
        // Fond global
        view = new VBox();
        view.setStyle("-fx-background-color:rgb(79, 137, 223);");
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));

        // Carte centrale
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 10, 0, 0, 5);");

        Label titleLabel = new Label("Welcome");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setTextFill(Color.web("#333"));

        // Username
        Label usernameLabel = new Label("Username");
        usernameLabel.setTextFill(Color.web("#666"));
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(280);
        usernameField.setStyle("-fx-background-radius: 5; -fx-border-radius: 10;");

        VBox usernameBox = new VBox(5, usernameLabel, usernameField);

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.web("#666"));
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(280);
        passwordField.setStyle("-fx-background-radius: 5; -fx-border-radius: 10;");

        VBox passwordBox = new VBox(5, passwordLabel, passwordField);

        // Button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(280);
        loginButton.setStyle(
                "-fx-background-color:rgb(79, 137, 223); -fx-text-fill: white; " +
                "-fx-background-radius: 5; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> handleLogin());

        // Message
        messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#E53935"));

        card.getChildren().addAll(titleLabel, usernameBox, passwordBox, loginButton, messageLabel);
        view.getChildren().add(card);
    }

    public VBox getView() {
        return view;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("1234")) {
            messageLabel.setTextFill(Color.web("#247, 127, 52"));
            messageLabel.setText("Connect !");
        } else {
            messageLabel.setTextFill(Color.web("#E53935"));
            messageLabel.setText("Error to connect, check your username or your password.");
        }
    }
}
