package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import utilities.SceneNavigator;
import java.io.IOException;
import java.util.List;

import dal.OrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class QCUMainController {

    @FXML
    private Button btnOpenReport, folderButton, homeButton;

    @FXML
    private ListView<String> toApproveListView;

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
            List<String> orders = OrderDAO.getFormattedOrderNumbers(); // <- trae los datos
            ObservableList<String> observableOrders = FXCollections.observableArrayList(orders);
            toApproveListView.setItems(observableOrders); // <- muestra los datos
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
