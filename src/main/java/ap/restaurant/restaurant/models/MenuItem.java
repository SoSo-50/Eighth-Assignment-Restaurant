package ap.restaurant.restaurant.models;

import javafx.beans.property.*;

public class MenuItem {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty category = new SimpleStringProperty();

    public MenuItem(int id, String name, String description, double price, String category) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.price.set(price);
        this.category.set(category);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getDescription() { return description.get(); }
    public double getPrice() { return price.get(); }
    public String getCategory() { return category.get(); }

    // Setters (اگر نیاز باشد)
    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setDescription(String description) { this.description.set(description); }
    public void setPrice(double price) { this.price.set(price); }
    public void setCategory(String category) { this.category.set(category); }

    // Property Getters!
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty categoryProperty() { return category; }

    @Override
    public String toString() {
        return name.get() + " - " + price.get() + " تومان";
    }
}