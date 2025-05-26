package gui.controllers;

import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import bll.OrderStatusManager;
import bll.ReportManager;
import be.Order;

import java.util.List;
import java.util.ArrayList;


import java.io.IOException;

public class AdminReportController {

    @FXML
    private Button signOutButton, userButton;

    @FXML
    private ListView<String> listViewReports;

    private final OrderStatusManager orderStatusManager = new OrderStatusManager();
    private final ReportManager reportManager = new ReportManager();


    public void initialize() {
        loadFinishedReports();
    }

    private void loadFinishedReports() {
        List<Order> doneOrders = orderStatusManager.getDoneOrders();
        List<String> reportSummaries = new ArrayList<>();

        for (Order order : doneOrders) {
            try {
                String orderCode = order.getOrderCode();
                String fullOrderNumber = order.getCountryNumber() + "/" +
                        order.getYear() + "/" +
                        order.getMonth() + "/" +
                        orderCode;

//                String comment = reportManager.getLatestCommentByOrderNumber(orderCode);
                reportSummaries.add("Order: " + fullOrderNumber); /* + " - Comment: " + comment);*/
            } catch (Exception e) {
                e.printStackTrace();
//                reportSummaries.add("Order: " + order.getOrderCode() + " - Error loading comment.");
            }
        }

        listViewReports.getItems().setAll(reportSummaries);
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
}
