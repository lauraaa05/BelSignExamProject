package gui.controllers;

import be.Operator;
import be.Order;
import be.QualityControl;
import dal.OrderStatusDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilities.SceneNavigator;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class QCUMainController {


    @FXML
    private Button btnOpenReport, folderButton, homeButton;

    @FXML
    private ListView<Order> toApproveListView;

    @FXML
    private Label welcomeLabel;

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    @FXML
    private void switchToFolderScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUFolderScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Folder Screen");
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    private void handleFolderButtonClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) folderButton.getScene().getWindow();
        try {
            switchToFolderScene(currentStage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSignOutButtonClick(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QCULogin.fxml"));
            Scene scene = new Scene(loader.load());
            currentStage.setTitle("QCU Login");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnOpenReportAction(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "QCUReport.fxml");
    }

    // Method to load orders into the ListView
    @FXML
    public void initialize() {
        try {
            OrderStatusDAO dao = new OrderStatusDAO();
            List<Order> orders = dao.getOrdersByRoleAndStatus("qcu", "to_approve");

            ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);
            toApproveListView.setItems(observableOrders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrderClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Order selectedOrder = toApproveListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                Stage stage = (Stage) toApproveListView.getScene().getWindow();
                sceneNavigator.<QCUNewReportController>switchToWithData(stage, "QCUNewReport.fxml", controller -> {
                    controller.setOrder(selectedOrder);
                });
            }
        }
    }

    public void setLoggedInQCU(QualityControl qcu) {
        welcomeLabel.setText("Welcome " + qcu.getFirstName());
    }
}