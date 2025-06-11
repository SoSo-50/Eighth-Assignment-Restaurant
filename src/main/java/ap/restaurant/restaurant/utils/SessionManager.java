package ap.restaurant.restaurant.utils;

import ap.restaurant.restaurant.models.User;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static User currentUser;
    private static Map<Integer, Integer> cart = new HashMap<>();

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
        cart.clear();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void addToCart(int menuItemId, int quantity) {
        cart.merge(menuItemId, quantity, Integer::sum);
    }

    public static void removeFromCart(int menuItemId) {
        cart.remove(menuItemId);
    }

    public static Map<Integer, Integer> getCart() {
        return new HashMap<>(cart);
    }

    public static void clearCart() {
        cart.clear();
    }

    public static int getCartItemCount() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }
}