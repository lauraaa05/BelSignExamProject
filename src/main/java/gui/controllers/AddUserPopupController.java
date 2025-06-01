package gui.controllers;

import be.Operator;
import be.QualityControl;
import be.User;
import bll.LoginManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddUserPopupController {

    @FXML private TextField firstNameField, lastNameField, usernameField, emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    private AdminUserController adminUserController;
    private final LoginManager loginManager =  new LoginManager();

    public void setAdminUserController(AdminUserController controller) {
        this.adminUserController = controller;
    }

    @FXML
    private void handleAddUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        String email = emailField.getText().trim();

        if(!email.matches("^\\S+@\\S+\\.\\S+$")) {
            showAlert("Invalid email address format.");
            return;
        }

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || role == null || email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
            return;
        }

        User newUser;
        if("Operator".equals(role)) {
            newUser = new Operator(firstName, lastName, username, password, email);
        } else {
            newUser = new QualityControl(firstName, lastName,username, password, email);
        }

        loginManager.addUser(newUser, password);
        adminUserController.refreshUserTable();
        closePopup();
        // Pass data to DAO or main controller here
        System.out.println("User created: " + firstName + " " + lastName + " (" + role + ")");

        // Close the popup
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
