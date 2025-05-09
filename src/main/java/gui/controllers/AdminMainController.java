package gui.controllers;

import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController {

    @FXML
    private Button signOutButton;

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        loadOrdersIntoToDoList();
//        loadOrdersIntoDoneList();
//
//        // Adding click event listener on the ListView
//        toDoListView.setOnMouseClicked(this::handleOrderClick);
//    }

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
