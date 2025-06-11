package ap.restaurant.restaurant;

import javafx.application.Application;
import javafx.stage.Stage;
import ap.restaurant.restaurant.utils.SceneManager;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchToLoginScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}