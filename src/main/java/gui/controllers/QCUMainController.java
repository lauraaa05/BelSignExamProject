package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.IOException;

public class QCUMainController {

    @FXML
    private Button btnOpenReport;

    private final SceneNavigator sceneNavigator = new SceneNavigator();

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
        sceneNavigator.switchTo(actionEvent,"QCUReport.fxml");
    }

    private void sceneNavigator(String s) {
    }
}

