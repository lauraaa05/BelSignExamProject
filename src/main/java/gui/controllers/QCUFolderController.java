package gui.controllers;

import dk.easv.belsignexamproject.OperatorLogInApp;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QCUFolderController {

    @FXML
    private FlowPane folderFlowPane;

    @FXML
    private Button signOutButton, homeButton;

    @FXML
    private Button folderButton;

    private final List<String> folderDates = new ArrayList<>();

    @FXML
    public void initialize() {

        highlightActiveButton(folderButton);
        for(int year = 2025; year <= 2026; year++) {
            for(int month = 1; month <= 12; month++) {
                folderDates.add(String.format("%d-%02d", year, month));
            }
        }

        for (String date : folderDates) {
            VBox folderBox = new VBox(5);
            folderBox.setAlignment(Pos.CENTER);
            folderBox.setPrefWidth(100);

            Button folderButton = new Button();
            ImageView icon = new ImageView(new Image(getClass().getResource("/images/folder.png").toExternalForm()));
            icon.setFitWidth(60);
            icon.setFitHeight(60);
            folderButton.setGraphic(icon);
            folderButton.getStyleClass().add("folder-button");

            folderButton.setOnAction(e -> System.out.println("Open folder: " + date));

            Label label = new Label(date);
            label.setStyle("-fx-font-size: 13px;");

            folderBox.getChildren().addAll(folderButton, label);
            folderFlowPane.getChildren().add(folderBox);
        }
    }

    @FXML
    private void switchToMainScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Folder Screen");
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    private void handleMainButtonClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) homeButton.getScene().getWindow();
        try {
            switchToMainScene(currentStage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSignOutButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        try {
            switchToLoginScene(currentStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToLoginScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCULogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Login");
        currentStage.setScene(scene);
        currentStage.show();
    }

    private void highlightActiveButton(Button activeButton) {
        folderButton.getStyleClass().remove("active");

        activeButton.getStyleClass().add("active");
    }
}

