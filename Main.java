import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.LoginPage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage();
        Scene scene = new Scene(loginPage.getView(), 600, 450);
        primaryStage.setTitle("LaplateformeTracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
