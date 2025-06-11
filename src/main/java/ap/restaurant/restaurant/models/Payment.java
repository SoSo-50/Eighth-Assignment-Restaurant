package ap.restaurant.restaurant.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Payment {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty orderId = new SimpleIntegerProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final StringProperty paymentMethod = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> paymentDate = new SimpleObjectProperty<>();
    private final ObjectProperty<PaymentStatus> status = new SimpleObjectProperty<>();

    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED,
        REFUNDED
    }

    public Payment() {
        this.status.set(PaymentStatus.PENDING);
    }

    public Payment(int orderId, double amount, String paymentMethod) {
        this();
        this.orderId.set(orderId);
        this.amount.set(amount);
        this.paymentMethod.set(paymentMethod);
        this.paymentDate.set(LocalDateTime.now());
    }

    // Getters
    public int getId() { return id.get(); }
    public int getOrderId() { return orderId.get(); }
    public double getAmount() { return amount.get(); }
    public String getPaymentMethod() { return paymentMethod.get(); }
    public LocalDateTime getPaymentDate() { return paymentDate.get(); }
    public PaymentStatus getStatus() { return status.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setOrderId(int orderId) { this.orderId.set(orderId); }
    public void setAmount(double amount) { this.amount.set(amount); }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod.set(paymentMethod); }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate.set(paymentDate); }
    public void setStatus(PaymentStatus status) { this.status.set(status); }

    // Property Getters
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty orderIdProperty() { return orderId; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    public ObjectProperty<LocalDateTime> paymentDateProperty() { return paymentDate; }
    public ObjectProperty<PaymentStatus> statusProperty() { return status; }

    @Override
    public String toString() {
        return String.format("Payment #%d - Order #%d - Amount: %,.0f - Method: %s - Status: %s",
                getId(), getOrderId(), getAmount(), getPaymentMethod(), getStatus());
    }
}