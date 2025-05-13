package gui.controllers;

import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminReportController {

    @FXML
    private Button signOutButton, userButton;

    @FXML
    private ListView<String> listViewReports;

//    @Override
    public void initialize() {
//        loadFinishedReports();
    }

//    private void loadFinishedReports() {
//        List<>
//    }

    private void switchToUserScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/AdminUserScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("User Management");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleUserButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) userButton.getScene().getWindow();
        switchToUserScreen(currentStage);
    }

    private void switchToLogInScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCULogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("Login");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleSignOutButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        switchToLogInScreen(currentStage);
    }
}
