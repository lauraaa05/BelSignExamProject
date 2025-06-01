package gui.controllers;

import be.User;
import dk.easv.belsignexamproject.MainLogin;
import exceptions.BLLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import bll.OrderStatusManager;
import bll.ReportManager;
import be.Order;
import utilities.LoggedInUser;
import utilities.SceneNavigator;

import java.util.List;
import java.util.ArrayList;


import java.io.IOException;
import java.util.stream.Collectors;

public class AdminReportController {

    @FXML
    private Button signOutButton, userButton;

    @FXML
    private TextField searchField;

    @FXML
    private Label loggedUsernameLbl;

    private final List<String> allReportSummaries = new ArrayList<>();

    @FXML
    private ListView<Order> listViewReports;

    private final OrderStatusManager orderStatusManager = new OrderStatusManager();
    private final ReportManager reportManager = new ReportManager();
    private final SceneNavigator sceneNavigator = new SceneNavigator();

    private final List<Order> allFinishedOrders = new ArrayList<>();


    public void initialize() {
        loadFinishedReports();

        // Adds a listener to the search field to filter the report list when the user is typing
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterReportList(newValue);
        });

        User user = LoggedInUser.getUser();
        if (user != null) {
            loggedUsernameLbl.setText(user.getFirstName());
        }
    }
    // Method to load finished orders into the ListView
    private void loadFinishedReports() {
        try {
            List<Order> doneOrders = orderStatusManager.getDoneOrders();
            allFinishedOrders.clear();
            allFinishedOrders.addAll(doneOrders);

            listViewReports.getItems().setAll(allFinishedOrders);

            listViewReports.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Order order, boolean empty) {
                    super.updateItem(order, empty);
                    if (empty || order == null) {
                        setText(null);
                    } else {
                        // Display full formatted order number
                        String fullOrderNumber = order.getCountryNumber() + "-" +
                                order.getYear() + "-" +
                                order.getMonth() + "-" +
                                order.getOrderCode();
                        setText("Order: " + fullOrderNumber);
                    }
                }
            });
        } catch (BLLException e) {
            showError("Failed to load finished reports: " + e.getMessage());
        }
    }

    // Method to filter the List based on the searchField text
    private void filterReportList(String searchText) {
        String lowerSearch = searchText.toLowerCase().trim();

        List<Order> filtered = allFinishedOrders.stream()
                .filter(order -> {
                    String fullOrderNumber = order.getCountryNumber() + "-" +
                            order.getYear() + "-" +
                            order.getMonth() + "-" +
                            order.getOrderCode();
                    return fullOrderNumber.toLowerCase().contains(lowerSearch);
                })
                .collect(Collectors.toList());

        listViewReports.getItems().setAll(filtered);
    }


    private void switchToUserScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainLogin.class.getResource("/view/AdminUserScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("User Management");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleUserButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) userButton.getScene().getWindow();
        switchToUserScreen(currentStage);
    }

    private void switchToLogInScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainLogin.class.getResource("/view/MainLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("Login");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleSignOutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        switchToLogInScreen(currentStage);
    }

    @FXML
    private void handleOrderClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Order selectedOrder = listViewReports.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                Stage stage = (Stage) listViewReports.getScene().getWindow();
                sceneNavigator.<QCUDoneReportController>switchToWithData(stage, "QCUDoneReport.fxml", "QCU Done Report", controller -> {
                    controller.setOrder(selectedOrder);
                });
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
