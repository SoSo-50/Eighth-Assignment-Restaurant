module ap.restaurant.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires jbcrypt;

    opens ap.restaurant.restaurant to javafx.fxml;
    opens ap.restaurant.restaurant.controllers to javafx.fxml;
    opens ap.restaurant.restaurant.models to javafx.base; // حیاتی برای PropertyValueFactory
    opens ap.restaurant.restaurant.dao to javafx.base; // اگر نیاز باشد
    opens ap.restaurant.restaurant.utils to javafx.fxml; // برای SceneManager و SessionManager
    opens ap.restaurant.restaurant.services to javafx.fxml; // اگر از طریق FXML به سرویس‌ها دسترسی دارید

    exports ap.restaurant.restaurant;
    exports ap.restaurant.restaurant.controllers;
    exports ap.restaurant.restaurant.services;
    exports ap.restaurant.restaurant.utils;
}