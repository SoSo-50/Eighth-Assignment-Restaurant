package ap.restaurant.restaurant.models;

import javafx.beans.property.*;

public class CartItem {
    private final ObjectProperty<MenuItem> menuItem = new SimpleObjectProperty<>();
    private final IntegerProperty quantity = new SimpleIntegerProperty(1);
    private final DoubleProperty totalPrice = new SimpleDoubleProperty(); // این Property قیمت کل را نگه می دارد

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem.set(menuItem);
        this.quantity.set(quantity);
        updateTotalPrice();
    }

    public MenuItem getMenuItem() { return menuItem.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getTotalPrice() { return totalPrice.get(); } // Getter برای totalPrice

    public void setMenuItem(MenuItem menuItem) { this.menuItem.set(menuItem); updateTotalPrice(); }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        updateTotalPrice();
    }

    public ObjectProperty<MenuItem> menuItemProperty() { return menuItem; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty totalPriceProperty() { return totalPrice; } // Property Getter برای totalPrice

    private void updateTotalPrice() {
        if (menuItem.get() != null) {
            totalPrice.set(menuItem.get().getPrice() * quantity.get());
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s (تعداد: %d - قیمت کل: %,.0f تومان)",
                menuItem.get().getName(), quantity.get(), totalPrice.get()
        );
    }
}