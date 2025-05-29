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
import javafx.scene.control.TextField;
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
        refreshLists();
        // Adding click event listener on the ListView
        toDoListView.setOnMouseClicked(this::handleOrderClick);

        be.User user = utilities.LoggedInUser.getUser();
        if (user != null) {
            loggedUsernameLbl.setText(user.getFirstName());
        }

        //To make rejected orders in red color
        toDoListView.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(order.toString());
                    String status = orderStatusDAO.getStatusForOrder(order.getOrderCode());
                    if ("rejected".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: #ffeeee;");

                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
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
    private void switchToMainLoginWindow(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/MainLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("MainLogin");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleSignOutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        switchToMainLoginWindow(currentStage);
    }

    public void setLoggedInOperator(Operator operator) {
        loggedUsernameLbl.setText(operator.getFirstName());
        System.out.println("Operator received in controller: " + operator.getFirstName());
    }

    private final String currentUserRole = "Operator";

    private final OrderStatusDAO  orderStatusDAO = new OrderStatusDAO();

    public void refreshLists() {
        OrderStatusDAO dao = new OrderStatusDAO();
        List<Order> todoOrders = dao.getOrdersByRoleAndStatuses("operator", List.of("todo","rejected"));

        todoOrders.sort((o1, o2) -> {
            String status1 = orderStatusDAO.getStatusForOrder(o1.getOrderCode());
            String status2 = orderStatusDAO.getStatusForOrder(o2.getOrderCode());

            if ("rejected".equalsIgnoreCase(status1) && !"rejected".equalsIgnoreCase(status2)) {
                return -1;
            } else if (!"rejected".equalsIgnoreCase(status1) && "rejected".equalsIgnoreCase(status2)) {
                return 1;
            } else {
                return 0;
            }
        });
        toDoListView.getItems().setAll(todoOrders);
    }
}