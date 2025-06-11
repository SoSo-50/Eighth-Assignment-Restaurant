package ap.restaurant.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ap.restaurant.restaurant.services.AuthService;
import ap.restaurant.restaurant.utils.SceneManager;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;
    @FXML private Label errorLabel;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("نام کاربری و رمز عبور نمی‌توانند خالی باشند.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("رمزهای عبور با هم مطابقت ندارند.");
            return;
        }

        try {
            if (AuthService.register(username, password, email)) {
                showAlert(Alert.AlertType.INFORMATION, "ثبت نام موفق", "حساب کاربری با موفقیت ایجاد شد!");
                SceneManager.switchToLoginScene();
            }
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage()); // مثلاً "نام کاربری قبلاً انتخاب شده است"
        } catch (SQLException e) {
            errorLabel.setText("خطا در ثبت نام: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException("خطا در تغییر صفحه پس از ثبت نام", e);
        }
    }

    @FXML
    private void switchToLogin() {
        try {
            SceneManager.switchToLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}