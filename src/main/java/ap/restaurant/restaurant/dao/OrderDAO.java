package ap.restaurant.restaurant.dao;

import ap.restaurant.restaurant.models.Order;
import ap.restaurant.restaurant.models.OrderDetail; // نیاز است
import ap.restaurant.restaurant.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public static int createOrder(int userId, double totalPrice) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, 'PENDING') RETURNING id";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDouble(2, totalPrice);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Creating order failed, no ID obtained.");
        }
    }

    public static void createOrderDetails(List<OrderDetail> details) throws SQLException {
        String sql = "INSERT INTO order_details (order_id, menu_item_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (OrderDetail detail : details) {
                pstmt.setInt(1, detail.getOrderId());
                pstmt.setInt(2, detail.getMenuItemId());
                pstmt.setInt(3, detail.getQuantity());
                pstmt.setDouble(4, detail.getPriceAtOrder()); // استفاده از getPriceAtOrder()
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        }
    }

    public static List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        }
        return orders;
    }

    public static boolean orderExists(int orderId) throws SQLException {
        String sql = "SELECT 1 FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public static boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                return order;
            }
            return null;
        }
    }
}