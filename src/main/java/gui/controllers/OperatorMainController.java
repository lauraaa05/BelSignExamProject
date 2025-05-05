package gui.controllers;

import be.Operator;
import bll.OrderManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OperatorMainController implements Initializable {

    @FXML
    private ListView<String> toDoListView;

    @FXML
    private Label loggedUsernameLbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrdersIntoToDoList();

        // Adding click event listener on the ListView
        toDoListView.setOnMouseClicked(this::handleOrderClick);
    }

    private void loadOrdersIntoToDoList() {
        OrderManager om = new OrderManager();
        List<String> orders = om.getOrderNumbersAsList();
        toDoListView.getItems().addAll(orders);
        toDoListView.setFixedCellSize(48);
    }

    // Handle order click
    private void handleOrderClick(MouseEvent event) {
        if (event.getClickCount() == 1) {  // Single click
            String selectedOrderNumber = toDoListView.getSelectionModel().getSelectedItem();
            if (selectedOrderNumber != null) {
                openOrderPreviewScene(selectedOrderNumber);
            }
        }
    }

    // Open the OperatorPreviewController scene and pass the order number
    private void openOrderPreviewScene(String orderNumber) {
        try {
            // Ensure this is the correct path to your FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OperatorPreview.fxml"));

            // Load the scene
            Scene orderPreviewScene = new Scene(loader.load());

            // Get the controller of the new scene and pass the order number
            OperatorPreviewController previewController = loader.getController();
            previewController.setOrderNumber(orderNumber);

            // Set the new scene to the current stage
            Stage stage = (Stage) toDoListView.getScene().getWindow();
            stage.setScene(orderPreviewScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoggedInOperator(Operator operator) {
        loggedUsernameLbl.setText(operator.getName());
    }
}




