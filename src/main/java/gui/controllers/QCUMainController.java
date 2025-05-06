package gui.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class QCUMainController {

    @FXML
    private Button folderButton, homeButton;

    @FXML
    private void initialize() {
        highlightActiveButton(homeButton);
    }

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

    private void highlightActiveButton(Button activeButton) {
        homeButton.getStyleClass().remove("active");
        activeButton.getStyleClass().add("active");
    }
}

