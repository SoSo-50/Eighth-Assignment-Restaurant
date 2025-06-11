package ap.restaurant.restaurant.models;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();

    public User(int id, String username, String password, String email) {
        this.id.set(id);
        this.username.set(username);
        this.password.set(password);
        this.email.set(email);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getEmail() { return email.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setUsername(String username) { this.username.set(username); }
    public void setPassword(String password) { this.password.set(password); }
    public void setEmail(String email) { this.email.set(email); }

    // Property Getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty emailProperty() { return email; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id.get() +
                ", username='" + username.get() + '\'' +
                ", email='" + email.get() + '\'' +
                '}';
    }
}