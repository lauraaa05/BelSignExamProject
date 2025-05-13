package gui.controllers;

import be.Operator;
import be.QualityControl;
import be.User;
import dal.LoginDAO;
import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminUserController implements Initializable {

    @FXML
    private TableView<User> tableViewUsers;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private Button signOutButton, addButton;

    private final LoginDAO loginDAO = new LoginDAO();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole()));

        List<User> allUsers = loginDAO.getAllUsers();
        tableViewUsers.setItems(FXCollections.observableArrayList(allUsers));
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

    @FXML
    private void handleAddUserPopup() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddUserPopup.fxml"));
        Parent root = fxmlLoader.load();

        AddUserPopupController controller = fxmlLoader.getController();
        controller.setAdminUserController(this);

        Stage stage = new Stage();
        stage.setTitle("Add User");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void refreshUserTable() {
        tableViewUsers.setItems(FXCollections.observableArrayList(loginDAO.getAllUsers()));
    }
}
