package gui.controllers;

import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import bll.OrderStatusManager;
import bll.ReportManager;
import be.Order;
import utilities.SceneNavigator;

import java.util.List;
import java.util.ArrayList;


import java.io.IOException;

public class AdminReportController {

    @FXML
    private Button signOutButton, userButton;

    @FXML
    private ListView<Order> listViewReports;

    private final OrderStatusManager orderStatusManager = new OrderStatusManager();
    private final ReportManager reportManager = new ReportManager();
    private final SceneNavigator sceneNavigator = new SceneNavigator();


    public void initialize() {
        loadFinishedReports();
    }

    private void loadFinishedReports() {
        List<Order> doneOrders = orderStatusManager.getDoneOrders();
        listViewReports.getItems().setAll(doneOrders);

        listViewReports.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                } else {
                    // Display full formatted order number
                    String fullOrderNumber = order.getCountryNumber() + "/" +
                            order.getYear() + "/" +
                            order.getMonth() + "/" +
                            order.getOrderCode();
                    setText("Order: " + fullOrderNumber);
                }
            }
        });
    }


    private void switchToUserScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/AdminUserScreen.fxml"));
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

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCULogin.fxml"));
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
                sceneNavigator.<QCUDoneReportController>switchToWithData(stage, "QCUDoneReport.fxml", controller -> {
                    controller.setOrder(selectedOrder);
                });
            }
        }
    }
}