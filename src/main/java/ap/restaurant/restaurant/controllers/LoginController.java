package ap.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ap.restaurant.restaurant.models.User;
import ap.restaurant.restaurant.services.AuthService;
import ap.restaurant.restaurant.utils.SceneManager;
import ap.restaurant.restaurant.utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = AuthService.login(username, password);
            if (user != null) {
                SessionManager.login(user);
                try {
                    SceneManager.switchToMenuScene();
                } catch (IOException e) {
                    errorLabel.setText("خطا در بارگذاری صفحه منو");
                    e.printStackTrace();
                }
            } else {
                errorLabel.setText("نام کاربری یا رمز عبور اشتباه است");
            }
        } catch (SQLException e) {
            errorLabel.setText("خطا در ارتباط با سرور");
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToRegister() {
        try {
            SceneManager.switchToRegisterScene();
        } catch (IOException e) {
            errorLabel.setText("خطا در بارگذاری صفحه ثبت نام");
            e.printStackTrace();
        }
    }
}