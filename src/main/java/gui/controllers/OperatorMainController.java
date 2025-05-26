package gui.controllers;


import be.Operator;
import be.Order;
import bll.OrderStatusManager;
import dal.OrderStatusDAO;
import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button signOutButton;

    @FXML
    private ListView<Order> toDoListView;

    @FXML
    private Label loggedUsernameLbl;

    public OperatorMainController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrdersIntoToDoList();

        // Adding click event listener on the ListView
        toDoListView.setOnMouseClicked(this::handleOrderClick);
    }

    private void loadOrdersIntoToDoList() {
        OrderStatusManager osm = new OrderStatusManager();
        List<Order> toDoOrders = osm.getToDoOrders();
        toDoListView.getItems().setAll(toDoOrders);
        toDoListView.setFixedCellSize(48);
    }

    // Handle order click
    private void handleOrderClick(MouseEvent event) {
        if (event.getClickCount() == 1) {  // Single click
            Order selectedOrder = toDoListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                openOrderPreviewScene(selectedOrder);
            }
        }
    }

    // Open the OperatorPreviewController scene and pass the order number
    private void openOrderPreviewScene(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OperatorPreview.fxml"));

            // Load the scene
            Scene orderPreviewScene = new Scene(loader.load());

            // Get the controller of the new scene and pass the order number
            OperatorPreviewController previewController = loader.getController();
            previewController.setOrder(order);

            // Set the new scene to the current stage
            Stage stage = (Stage) toDoListView.getScene().getWindow();
            stage.setScene(orderPreviewScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void switchToMainSceneSameWindow(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/OperatorLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("OperatorLogin");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleSignOutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        switchToMainSceneSameWindow(currentStage);
    }

    public void setLoggedInOperator(Operator operator) {
        loggedUsernameLbl.setText(operator.getFirstName());
    }

    private final String currentUserRole = "Operator";

    private final OrderStatusDAO  orderStatusDAO = new OrderStatusDAO();

    public void refreshLists() {
        OrderStatusDAO dao = new OrderStatusDAO();
        List<Order> todoOrders = dao.getOrdersByRoleAndStatus("operator", "todo");

        toDoListView.getItems().setAll(todoOrders);
    }
}