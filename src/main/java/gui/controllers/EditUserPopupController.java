package gui.controllers;

import bll.LoginManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import be.User;

public class EditUserPopupController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;

    private User user; // The user being edited
    private AdminUserController adminUserController;
    public void setAdminUserController(AdminUserController controller) {
        this.adminUserController = controller;
    }

    public void setUser(User user) {
        this.user = user;
        // Pre-fill fields
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        usernameField.setText(user.getName());
        passwordField.setText(user.getPassword()); // Or leave blank for security
        emailField.setText(user.getEmail());
        roleComboBox.setValue(user.getRole());
    }

    @FXML
    private void handleCancel() {
        ((Stage) firstNameField.getScene().getWindow()).close();
    }

    @FXML
    private void handleUpdateUser() {
        // Update user object
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        user.setName(usernameField.getText());
        user.setPassword(passwordField.getText());
        user.setEmail(emailField.getText());
        user.setRole(roleComboBox.getValue());

        LoginManager loginManager = new LoginManager();
        boolean success = loginManager.updateUser(user);
        adminUserController.refreshUserTable();

        if(!success) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update user in the database.");
            alert.showAndWait();
            return;
        } else {
            System.out.println("User updated: " + user.getFirstName() + " " + user.getLastName());
        }

        ((Stage) firstNameField.getScene().getWindow()).close();
    }
}