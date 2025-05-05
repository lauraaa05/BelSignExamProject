package gui.controllers;

import be.Operator;
import bll.OrderManager;
import dal.OrderStatusDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OperatorMainController implements Initializable {

    @FXML
    private ListView<String> toDoListView;

    @FXML
    private ListView<String> doneListView;

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

    private final String currentUserRole = "Operator";

    private final OrderStatusDAO  orderStatusDAO = new OrderStatusDAO();

    @FXML
    private void onMarkAsDone() {
        String selectedOrder = toDoListView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Mark order as done?");
            alert.setContentText("Do you want to mark this " + selectedOrder + "as done?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                orderStatusDAO.updateOrderStatus(selectedOrder, currentUserRole, "done");
                refreshLists();
            }
        }
    }

    private void refreshLists() {
        List<String> toDo = orderStatusDAO.getOrdersByRoleAndStatus(currentUserRole, "todo");
        List<String> done = orderStatusDAO.getOrdersByRoleAndStatus(currentUserRole, "done");

        toDoListView.getItems().setAll(toDo);
        doneListView.getItems().setAll(done);
    }
}




