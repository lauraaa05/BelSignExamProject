package gui.controllers;

import be.Admin;
import be.User;
import bll.LoginManager;
import dk.easv.belsignexamproject.MainLogin;
import exceptions.BLLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.LoggedInUser;
import utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
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
    private Label welcomeLabel;

    @FXML
    private Button signOutButton, addButton, editButton, deleteUserButton, reportButton;

    private final LoginManager loginManager = new LoginManager();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole()));

        try {
            List<User> allUsers = loginManager.getAllUsers();
            tableViewUsers.setItems(FXCollections.observableArrayList(allUsers));
        } catch (BLLException e) {
            showError("Failed to load users: " + e.getMessage());
        }

        deleteUserButton.setDisable(true);
        tableViewUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteUserButton.setDisable(newSelection == null);
        });

        User user = LoggedInUser.getUser();
        if (user != null) {
            welcomeLabel.setText("Welcome " + user.getFirstName());
        }
    }

    private void switchToLogInScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainLogin.class.getResource("/view/MainLogin.fxml"));
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

    @FXML
    private void handleEditButtonClick(ActionEvent event) throws IOException {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            handleEditUserPopup(selectedUser);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a user to edit.");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleEditUserPopup(User selectedUser) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditUserPopup.fxml"));
        Parent root = loader.load();

        EditUserPopupController controller = loader.getController();
        controller.setUser(selectedUser);
        controller.setAdminUserController(this);

        Stage stage = new Stage();
        stage.setTitle("Edit User");
        stage.setScene(new Scene(root));
        stage.show();

        refreshUserTable();
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

        if(selectedUser == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete confirmation");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("User: " + selectedUser.getFirstName() + " " + selectedUser.getLastName());

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(type -> {
            if(type == yesButton) {
                loginManager.deleteUser(selectedUser);
                refreshUserTable();
            }
        });
    }

    public void refreshUserTable() {
        try {
            tableViewUsers.setItems(FXCollections.observableArrayList(loginManager.getAllUsers()));
        } catch (BLLException e) {
            showError("Failed to refresh user table: " + e.getMessage());
        }
    }

    private void switchToReportScreen(Stage currentStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainLogin.class.getResource("/view/AdminReport.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("Admin Report Screen");
        currentStage.setScene(scene);
        currentStage.show();

    }

    @FXML
    public void handleReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) reportButton.getScene().getWindow();
        switchToReportScreen(currentStage);
    }

    public void setLoggedInAdmin(Admin admin) {
        welcomeLabel.setText("Welcome " + admin.getFirstName());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }
}