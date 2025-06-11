package ap.restaurant.restaurant.dao;

import ap.restaurant.restaurant.models.Payment;
import ap.restaurant.restaurant.models.Payment.PaymentStatus;
import ap.restaurant.restaurant.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public static boolean createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, amount, payment_method, payment_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, payment.getOrderId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setTimestamp(4, Timestamp.valueOf(payment.getPaymentDate()));
            pstmt.setString(5, payment.getStatus().name());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public static Payment getPaymentById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            return null;
        }
    }

    public static List<Payment> getPaymentsByOrder(int orderId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY payment_date DESC";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    public static boolean updatePaymentStatus(int paymentId, PaymentStatus status) throws SQLException {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, paymentId);

            return pstmt.executeUpdate() > 0;
        }
    }

    public static PaymentStatus getPaymentStatusByOrder(int orderId) throws SQLException {
        String sql = "SELECT status FROM payments WHERE order_id = ? ORDER BY payment_date DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() ? PaymentStatus.valueOf(rs.getString("status")) : PaymentStatus.FAILED;
        }
    }

    public static Payment getPaymentByOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY payment_date DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            return null;
        }
    }

    private static Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        return payment;
    }

    public static boolean paymentExistsForOrder(int orderId) throws SQLException {
        String sql = "SELECT 1 FROM payments WHERE order_id = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            return pstmt.executeQuery().next();
        }
    }
}