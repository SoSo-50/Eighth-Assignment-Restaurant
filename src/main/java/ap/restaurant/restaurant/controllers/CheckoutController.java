package ap.restaurant.restaurant.controllers;

import ap.restaurant.restaurant.dao.MenuItemDAO;
import ap.restaurant.restaurant.models.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ap.restaurant.restaurant.models.*;
import ap.restaurant.restaurant.services.*;
import ap.restaurant.restaurant.utils.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutController {

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> itemNameColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Double> totalColumn;
    @FXML private Label totalLabel; // اصلاح: اضافه شدن اعلامیه totalLabel
    @FXML private Button confirmButton;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCartItems();
        updateTotal();
    }

    private void setupTableColumns() {
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMenuItem().nameProperty());

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // فرمت نمایش قیمت‌ها
        priceColumn.setCellFactory(column -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("%,.0f تومان", price));
            }
        });

        totalColumn.setCellFactory(column -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                setText(empty ? "" : String.format("%,.0f تومان", total));
            }
        });
    }

    private void loadCartItems() {
        Map<Integer, Integer> cartItems = SessionManager.getCart();

        cartTable.getItems().setAll(
                cartItems.entrySet().stream()
                        .map(entry -> {
                            try {
                                MenuItem menuItem = MenuItemDAO.getMenuItemById(entry.getKey());
                                return new CartItem(menuItem, entry.getValue());
                            } catch (SQLException e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(item -> item != null)
                        .collect(Collectors.toList())
        );
    }

    private void updateTotal() {
        double total = cartTable.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        totalLabel.setText(String.format("جمع کل: %,.0f تومان", total));
    }

    @FXML
    private void handleConfirmOrder() {
        try {
            int orderId = OrderService.placeOrder();

            showSuccessAlert(
                    "سفارش ثبت شد",
                    "شماره سفارش شما: " + orderId + "\nاز خرید شما متشکریم!"
            );

            SceneManager.switchToMenuScene();

        } catch (SQLException e) {
            showErrorAlert("خطا در ثبت سفارش", e.getMessage());
            e.printStackTrace(); // برای دیباگ
        } catch (IOException e) {
            throw new RuntimeException("خطا در تغییر صفحه پس از ثبت سفارش", e);
        }
    }

    @FXML
    private void handleReturnToMenu() throws IOException {
        SceneManager.switchToMenuScene();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}