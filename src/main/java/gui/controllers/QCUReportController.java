package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utilities.SceneNavigator;

public class QCUReportController {

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    @FXML
    private void goBackButtonAction(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "QCUMain.fxml");
    }

    public void setOrderNumber(String orderNumber) {
        System.out.println("setOrder: " + orderNumber);
    }
}
