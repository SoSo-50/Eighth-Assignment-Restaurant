package ap.restaurant.restaurant.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final DoubleProperty totalPrice = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty("PENDING"); // PENDING, PAID, CANCELLED, COMPLETED
    private final ListProperty<OrderDetail> orderDetails = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Order() {
        this.createdAt.set(LocalDateTime.now());
    }

    public Order(int userId, double totalPrice) {
        this();
        this.userId.set(userId);
        this.totalPrice.set(totalPrice);
    }

    // Getters
    public int getId() { return id.get(); }
    public int getUserId() { return userId.get(); }
    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public double getTotalPrice() { return totalPrice.get(); }
    public String getStatus() { return status.get(); }
    public List<OrderDetail> getOrderDetails() { return orderDetails.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }
    public void setTotalPrice(double totalPrice) { this.totalPrice.set(totalPrice); }
    public void setStatus(String status) { this.status.set(status); }
    public void setOrderDetails(List<OrderDetail> details) { this.orderDetails.setAll(details); }

    // Property Getters
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty userIdProperty() { return userId; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public DoubleProperty totalPriceProperty() { return totalPrice; }
    public StringProperty statusProperty() { return status; }
    public ListProperty<OrderDetail> orderDetailsProperty() { return orderDetails; }

    // Helper Methods
    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
        recalculateTotal();
    }

    public void removeOrderDetail(OrderDetail detail) {
        this.orderDetails.remove(detail);
        recalculateTotal();
    }

    private void recalculateTotal() {
        double total = this.orderDetails.stream()
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();
        this.totalPrice.set(total);
    }

    // Status Check Methods
    public boolean isPending() { return "PENDING".equalsIgnoreCase(getStatus()); }
    public boolean isPaid() { return "PAID".equalsIgnoreCase(getStatus()); }
    public boolean isCancelled() { return "CANCELLED".equalsIgnoreCase(getStatus()); }

    // toString
    @Override
    public String toString() {
        return String.format(
                "Order #%d - User: %d - Total: %,.0f - Status: %s - Items: %d",
                getId(), getUserId(), getTotalPrice(), getStatus(), getOrderDetails().size()
        );
    }
}