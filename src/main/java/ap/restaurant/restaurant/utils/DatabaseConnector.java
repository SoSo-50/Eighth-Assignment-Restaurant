package ap.restaurant.restaurant.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Soheil005"; // رمز عبور PostgreSQL

    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DEBUG: Connection to database established successfully!"); // <<< این خط را اضافه کنید
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
    }

    public static void testConnection() {
        try (Connection conn = connect()) {
            System.out.println("اتصال به دیتابیس با موفقیت انجام شد!");
        } catch (SQLException e) {
            System.err.println("خطا در اتصال به دیتابیس: " + e.getMessage());
        }
    }
}