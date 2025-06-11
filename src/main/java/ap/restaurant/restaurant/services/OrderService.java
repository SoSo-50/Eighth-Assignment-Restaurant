package ap.restaurant.restaurant.services;

import ap.restaurant.restaurant.dao.*;
import ap.restaurant.restaurant.models.*;
import ap.restaurant.restaurant.utils.SessionManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    public static int placeOrder() throws SQLException {
        double total = calculateOrderTotal();
        int orderId = OrderDAO.createOrder(
                SessionManager.getCurrentUser().getId(),
                total
        );

        List<OrderDetail> details = prepareOrderDetails(orderId);
        OrderDetailDAO.createOrderDetails(details);
        SessionManager.clearCart();
        return orderId;
    }

    private static double calculateOrderTotal() {
        return SessionManager.getCart().entrySet().stream()
                .mapToDouble(entry -> {
                    try {
                        MenuItem item = MenuItemDAO.getMenuItemById(entry.getKey());
                        return item.getPrice() * entry.getValue();
                    } catch (SQLException e) {
                        throw new RuntimeException("Error calculating total", e);
                    }
                }).sum();
    }

    private static List<OrderDetail> prepareOrderDetails(int orderId) {
        return SessionManager.getCart().entrySet().stream()
                .map(entry -> {
                    try {
                        MenuItem item = MenuItemDAO.getMenuItemById(entry.getKey());
                        OrderDetail detail = new OrderDetail();
                        detail.setOrderId(orderId);
                        detail.setMenuItemId(item.getId());
                        detail.setQuantity(entry.getValue());
                        detail.setPrice(item.getPrice());
                        return detail;
                    } catch (SQLException e) {
                        throw new RuntimeException("Error preparing order details", e);
                    }
                })
                .collect(Collectors.toList());
    }
}