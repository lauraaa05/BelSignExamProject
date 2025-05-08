package gui.controllers;

import be.Operator;
import bll.OrderManager;
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
    private ListView<String> toDoListView;

    @FXML
    private ListView<String> doneListView;

    @FXML
    private Label loggedUsernameLbl;

    public OperatorMainController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrdersIntoToDoList();
        loadOrdersIntoDoneList();

        // Adding click event listener on the ListView
        toDoListView.setOnMouseClicked(this::handleOrderClick);
    }

    private void loadOrdersIntoToDoList() {
        OrderStatusManager osm = new OrderStatusManager();
        List<String> toDoOrders = osm.getToDoOrders();
        toDoListView.getItems().setAll(toDoOrders);
        toDoListView.setFixedCellSize(48);
    }

    private void loadOrdersIntoDoneList() {
        OrderStatusManager osm = new OrderStatusManager();
        List<String> doneOrders = osm.getDoneOrders();
        doneListView.getItems().setAll(doneOrders);
        doneListView.setFixedCellSize(48);
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
        loggedUsernameLbl.setText(operator.getName());
    }

    private final String currentUserRole = "Operator";

    private final OrderStatusDAO  orderStatusDAO = new OrderStatusDAO();

    public void refreshLists() {
        OrderStatusDAO dao = new OrderStatusDAO();
        List<String> todoOrders = dao.getFormattedOrdersByRoleAndStatus("operator", "todo");
        List<String> doneOrders = dao.getFormattedOrdersByRoleAndStatus("operator", "done");

        toDoListView.getItems().setAll(todoOrders);
        doneListView.getItems().setAll(doneOrders);
    }
}




