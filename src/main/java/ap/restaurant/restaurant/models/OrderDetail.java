package ap.restaurant.restaurant.models;

import javafx.beans.property.*;

public class OrderDetail {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty orderId = new SimpleIntegerProperty();
    private final IntegerProperty menuItemId = new SimpleIntegerProperty();
    private final ObjectProperty<MenuItem> menuItem = new SimpleObjectProperty<>();
    private final IntegerProperty quantity = new SimpleIntegerProperty(1);
    private final DoubleProperty price = new SimpleDoubleProperty(); // این فیلد برای priceAtOrder استفاده می شود

    public OrderDetail() {}

    public OrderDetail(int orderId, int menuItemId, int quantity, double price) {
        this.orderId.set(orderId);
        this.menuItemId.set(menuItemId);
        this.quantity.set(quantity);
        this.price.set(price);
    }

    // Getters
    public int getId() { return id.get(); }
    public int getOrderId() { return orderId.get(); }
    public int getMenuItemId() { return menuItemId.get(); }
    public MenuItem getMenuItem() { return menuItem.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getPrice() { return price.get(); }

    public double getPriceAtOrder() { return price.get(); } // متد برای priceAtOrder

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setOrderId(int orderId) { this.orderId.set(orderId); }
    public void setMenuItemId(int menuItemId) { this.menuItemId.set(menuItemId); }
    public void setMenuItem(MenuItem menuItem) { this.menuItem.set(menuItem); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public void setPrice(double price) { this.price.set(price); }
    public void setPriceAtOrder(double priceAtOrder) { this.price.set(priceAtOrder); } // Setter برای priceAtOrder


    // Property Getters
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty orderIdProperty() { return orderId; }
    public IntegerProperty menuItemIdProperty() { return menuItemId; }
    public ObjectProperty<MenuItem> menuItemProperty() { return menuItem; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty priceProperty() { return price; }

    // Helper Methods
    public double getTotal() { return getPrice() * getQuantity(); }

    @Override
    public String toString() {
        return String.format(
                "OrderDetail #%d - Item: %d x %d @ %,.0f = %,.0f",
                getId(), getMenuItemId(), getQuantity(), getPrice(), getTotal()
        );
    }
}