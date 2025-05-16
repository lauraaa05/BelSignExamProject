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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class OperatorMainController implements Initializable {

    @FXML
    private Button signOutButton;

    @FXML
    private ListView<String> toDoListView;

    @FXML
    private Label loggedUsernameLbl;

    @FXML
    private TextField searchField;

    private final OrderStatusDAO orderStatusDAO = new OrderStatusDAO();
    private final String currentUserRole = "operator";
    private final String currentStatus = "todo";

    private List<String> allToDoOrders = new ArrayList<>();

    public OperatorMainController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrdersIntoToDoList();

        // Set up list view click behavior
        toDoListView.setOnMouseClicked(this::handleOrderClick);

        // Live search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterOrders());
    }

    private void loadOrdersIntoToDoList() {
        allToDoOrders = orderStatusDAO.getFormattedOrdersByRoleAndStatus(currentUserRole, currentStatus);
        toDoListView.getItems().setAll(allToDoOrders);
        toDoListView.setFixedCellSize(48);
    }

    private void filterOrders() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            toDoListView.getItems().setAll(allToDoOrders);
        } else {
            List<String> filtered = allToDoOrders.stream()
                    .filter(order -> order.toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            toDoListView.getItems().setAll(filtered);
        }
    }

    @FXML
    public void searchBtnAction(ActionEvent actionEvent) {
        filterOrders();
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

    @FXML
    public void handleSignOutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        switchToMainSceneSameWindow(currentStage);
    }

    private void switchToMainSceneSameWindow(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/OperatorLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("OperatorLogin");
        currentStage.setScene(scene);
        currentStage.show();
    }

    public void setLoggedInOperator(Operator operator) {
        loggedUsernameLbl.setText(operator.getName());
    }

    public void refreshLists() {
        loadOrdersIntoToDoList();
    }
}
