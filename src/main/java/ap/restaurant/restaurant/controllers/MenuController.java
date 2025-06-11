package ap.restaurant.restaurant.controllers;

import ap.restaurant.restaurant.services.MenuService;
import ap.restaurant.restaurant.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ap.restaurant.restaurant.dao.MenuItemDAO;
import ap.restaurant.restaurant.models.MenuItem;
import ap.restaurant.restaurant.utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MenuController {
    @FXML private ListView<MenuItem> menuListView;
    @FXML private ListView<String> cartListView;
    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        loadMenuItems();
        updateCartView();
    }

    private void loadMenuItems() {
        try {
            List<MenuItem> items = MenuService.getFullMenu();
            menuListView.getItems().setAll(items);
        } catch (SQLException e) {
            e.printStackTrace();


        }
    }

    @FXML
    private void addToCart() {
        MenuItem selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SessionManager.addToCart(selected.getId(), 1);
            updateCartView();
        }
    }

    private void updateCartView() {
        cartListView.getItems().clear();
        double total = 0;

        for (Map.Entry<Integer, Integer> entry : SessionManager.getCart().entrySet()) {
            try {
                MenuItem item = MenuItemDAO.getMenuItemById(entry.getKey());
                String cartItem = item.getName() + " x" + entry.getValue() + " - " + (item.getPrice() * entry.getValue()) + " تومان";
                cartListView.getItems().add(cartItem);
                total += item.getPrice() * entry.getValue();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        totalLabel.setText("جمع کل: " + total + " تومان");
    }

    @FXML
    private void proceedToCheckout() {
        try {
            SceneManager.switchToCheckoutScene();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void logout() {
        SessionManager.logout();
        try {
            SceneManager.switchToLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void viewOrderHistory() {
        try {
            SceneManager.switchToOrderHistoryScene();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}