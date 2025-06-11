package ap.restaurant.restaurant.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ap.restaurant.restaurant.models.*;
import ap.restaurant.restaurant.dao.*;
import ap.restaurant.restaurant.utils.SessionManager;
import ap.restaurant.restaurant.utils.SceneManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderController {
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, LocalDateTime> dateColumn;
    @FXML private TableColumn<Order, Double> totalColumn;
    @FXML private TableColumn<Order, String> statusColumn;

    @FXML private TableView<OrderDetail> itemsTable;
    @FXML private TableColumn<OrderDetail, String> itemNameColumn;
    @FXML private TableColumn<OrderDetail, Integer> quantityColumn;
    @FXML private TableColumn<OrderDetail, Double> priceColumn;
    @FXML private TableColumn<OrderDetail, Double> subtotalColumn;

    @FXML
    public void initialize() {
        setupOrdersTable();
        setupOrderDetailsTable();
        loadUserOrders();
    }

    private void setupOrdersTable() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // فرمت دهی تاریخ و مبلغ
        dateColumn.setCellFactory(column -> new TableCell<Order, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : formatter.format(item));
            }
        });

        totalColumn.setCellFactory(column -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.format("%,.0f تومان", item));
            }
        });

        // انتخاب سفارش و نمایش آیتم‌های آن
        ordersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> showOrderDetails(newSelection)
        );
    }

    private void setupOrderDetailsTable() {
        // برای نمایش نام MenuItem از داخل OrderDetail
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMenuItem().nameProperty());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price")); // قیمت واحد در OrderDetail
        subtotalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject()); // جمع کل هر آیتم

        // فرمت دهی مبلغ
        priceColumn.setCellFactory(column -> new TableCell<OrderDetail, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.format("%,.0f تومان", item));
            }
        });
        subtotalColumn.setCellFactory(column -> new TableCell<OrderDetail, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.format("%,.0f تومان", item));
            }
        });
    }

    private void loadUserOrders() {
        if (SessionManager.getCurrentUser() == null) {
            showAlert("خطا", "ابتدا وارد حساب کاربری خود شوید.");
            return;
        }
        try {
            List<Order> orders = OrderDAO.getOrdersByUserId(SessionManager.getCurrentUser().getId());
            ordersTable.getItems().setAll(orders);
        } catch (SQLException e) {
            showAlert("خطا در بارگذاری سفارشات", "خطای دیتابیس: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showOrderDetails(Order order) {
        if (order != null) {
            try {
                List<OrderDetail> details = OrderDetailDAO.getDetailsByOrderId(order.getId());
                itemsTable.getItems().setAll(details);
            } catch (SQLException e) {
                showAlert("خطا در بارگذاری جزئیات", "خطای دیتابیس: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void returnToMenu() {
        try {
            SceneManager.switchToMenuScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}