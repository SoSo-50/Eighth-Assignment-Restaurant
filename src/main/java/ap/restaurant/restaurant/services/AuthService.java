package ap.restaurant.restaurant.services;

import ap.restaurant.restaurant.dao.UserDAO;
import ap.restaurant.restaurant.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AuthService {
    public static User login(String username, String password) throws SQLException {
        User user = UserDAO.getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public static boolean register(String username, String password, String email)
            throws SQLException, IllegalArgumentException {

        if (UserDAO.usernameExists(username)) {
            throw new IllegalArgumentException("نام کاربری قبلاً انتخاب شده است");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return UserDAO.createUser(username, hashedPassword, email);
    }

    public static boolean changePassword(String username, String currentPassword, String newPassword)
            throws SQLException {

        User user = login(username, currentPassword);
        if (user == null) {
            return false;
        }

        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return UserDAO.updatePassword(username, newHashedPassword);
    }
}