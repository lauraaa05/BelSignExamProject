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
import javafx.scene.control.*;
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

    @FXML
    private TextField yearSearchField;

    private final String currentUserRole = "Operator";
    private final OrderStatusDAO orderStatusDAO = new OrderStatusDAO();

    public OperatorMainController() {
        // No-arg constructor
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrdersIntoToDoList();
        loadOrdersIntoDoneList();

        // Add click listener
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

    private void handleOrderClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            String selectedOrderNumber = toDoListView.getSelectionModel().getSelectedItem();
            if (selectedOrderNumber != null) {
                openOrderPreviewScene(selectedOrderNumber);
            }
        }
    }

    private void openOrderPreviewScene(String orderNumber) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OperatorPreview.fxml"));
            Scene orderPreviewScene = new Scene(loader.load());

            OperatorPreviewController previewController = loader.getController();
            previewController.setOrderNumber(orderNumber);

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

    public void refreshLists() {
        List<String> todoOrders = orderStatusDAO.getFormattedOrdersByRoleAndStatus("operator", "todo");
        List<String> doneOrders = orderStatusDAO.getFormattedOrdersByRoleAndStatus("operator", "done");

        toDoListView.getItems().setAll(todoOrders);
        doneListView.getItems().setAll(doneOrders);
    }

    @FXML
    private void handleSearchByYearAction() {
        String year = yearSearchField.getText().trim();

        if (year.isEmpty()) {
            System.out.println("Year is empty");
            return;
        }

        // Simulate search - replace with your actual DB call
        List<String> orders = getOrdersByYear(year);

        toDoListView.getItems().clear();
        toDoListView.getItems().addAll(orders);
    }

    private List<String> getOrdersByYear(String year) {
        return orderStatusDAO.getOrdersByYear(year);
    }

    public void handleSearchAction(ActionEvent actionEvent) {
    }

    public void handleLogoutAction(ActionEvent actionEvent) {
    }
    public void handleSearchYearAction(ActionEvent actionEvent) {
    }
}
