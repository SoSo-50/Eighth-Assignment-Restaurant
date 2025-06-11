package ap.restaurant.restaurant.dao;

import ap.restaurant.restaurant.utils.DatabaseConnector;
import ap.restaurant.restaurant.models.User;
import java.sql.*;

public class UserDAO {
    public static boolean createUser(String username, String hashedPassword, String email) throws SQLException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);

            return pstmt.executeUpdate() > 0;
        }
    }

    public static User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
            return null;
        }
    }

    public static boolean usernameExists(String username) throws SQLException {
        return getUserByUsername(username) != null;
    }

    public static boolean updatePassword(String username, String newHashedPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newHashedPassword);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;
        }
    }
}