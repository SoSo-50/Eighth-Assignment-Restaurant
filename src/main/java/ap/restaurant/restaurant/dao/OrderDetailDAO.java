package ap.restaurant.restaurant.dao;

import ap.restaurant.restaurant.models.OrderDetail;
import ap.restaurant.restaurant.utils.DatabaseConnector;
import ap.restaurant.restaurant.models.MenuItem; // اضافه شد برای mapResultSetToOrderDetail

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    // نام متد از createOrderDetail به createOrderDetails (جمع) تغییر یافته است
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

    public static List<OrderDetail> getDetailsByOrderId(int orderId) throws SQLException {
        List<OrderDetail> details = new ArrayList<>();
        // کوئری JOIN برای دریافت اطلاعات MenuItem نیز اضافه شد
        String sql = "SELECT od.*, mi.name as menu_name, mi.description as menu_description, mi.price as menu_price, mi.category as menu_category " +
                "FROM order_details od JOIN menu_items mi ON od.menu_item_id = mi.id " +
                "WHERE od.order_id = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                details.add(mapResultSetToOrderDetail(rs));
            }
        }
        return details;
    }

    // متد کمکی برای نگاشت ResultSet به OrderDetail (جدید)
    private static OrderDetail mapResultSetToOrderDetail(ResultSet rs) throws SQLException {
        OrderDetail detail = new OrderDetail();
        detail.setId(rs.getInt("id"));
        detail.setOrderId(rs.getInt("order_id"));
        detail.setMenuItemId(rs.getInt("menu_item_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setPrice(rs.getDouble("price")); // قیمت در زمان سفارش

        // ایجاد شیء MenuItem و تنظیم آن در OrderDetail
        MenuItem menuItem = new MenuItem(
                rs.getInt("menu_item_id"),
                rs.getString("menu_name"),
                rs.getString("menu_description"),
                rs.getDouble("menu_price"), // قیمت فعلی آیتم منو
                rs.getString("menu_category")
        );
        detail.setMenuItem(menuItem);

        return detail;
    }
}