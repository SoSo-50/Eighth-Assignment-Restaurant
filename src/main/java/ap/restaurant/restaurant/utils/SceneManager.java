package ap.restaurant.restaurant.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchToLoginScene() throws IOException {
        loadScene("/ap/restaurant/restaurant/views/login.fxml", "ورود به سیستم");
    }

    public static void switchToMenuScene() throws IOException {
        loadScene("/ap/restaurant/restaurant/views/menu.fxml", "منوی رستوران");
    }

    public static void switchToCheckoutScene() throws IOException {
        loadScene("/ap/restaurant/restaurant/views/checkout.fxml", "تسویه حساب");
    }

    public static void switchToRegisterScene() throws IOException {
        loadScene("/ap/restaurant/restaurant/views/register.fxml", "ثبت‌نام کاربر");
    }

    public static void switchToOrderHistoryScene() throws IOException { // اضافه شد
        loadScene("/ap/restaurant/restaurant/views/order-history.fxml", "تاریخچه سفارشات");
    }


    private static void loadScene(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource(fxmlPath)));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(Objects.requireNonNull(
                SceneManager.class.getResource("/ap/restaurant/restaurant/styles.css")
        ).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
    }
}