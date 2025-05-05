package gui.controllers;

import bll.LoginManager;
import dal.LoginDAO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javafx.scene.image.Image;

import java.net.URL;

public class QCULogInController {


    @FXML
    private TextField usernameField;


    @FXML
    private PasswordField passwordFieldMasked;

    @FXML
    private TextField passwordFieldVisible;

    @FXML
    private Label errorLabel;



    @FXML
    private Label eyeLabel;
//    @FXML
//    private Label eyeLabel;

    @FXML
    private MFXButton logInButton;

    private boolean passwordVisible = false;
    private LoginManager loginManager = new LoginManager();



    @FXML
    private void initialize() {
        errorLabel.setVisible(false);

        passwordFieldMasked.setVisible(true);
        passwordFieldVisible.setVisible(false);

        // Set initial eye icon
        setEyeIcon("eyeOpened.png");

        logInButton.setOnAction(e -> handleLogin());
        eyeLabel.setOnMouseClicked(e -> togglePasswordVisibility());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordVisible
                ? passwordFieldVisible.getText().trim()
                : passwordFieldMasked.getText().trim();

        boolean isValid = loginManager.checkQCULogin(username, password);

        if (isValid) {
            errorLabel.setVisible(false);
            System.out.println("Login Successful"); // TODO: Load QCU main scene here
        } else {
            errorLabel.setText("Incorrect username or password");
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(true);
        }
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordFieldMasked.setText(passwordFieldVisible.getText());
            passwordFieldMasked.setVisible(true);
            passwordFieldVisible.setVisible(false);
            setEyeIcon("eyeOpened.png");
        } else {
            passwordFieldVisible.setText(passwordFieldMasked.getText());
            passwordFieldVisible.setVisible(true);
            passwordFieldMasked.setVisible(false);
            setEyeIcon("eyeClosed.png");
        }
        passwordVisible = !passwordVisible;
    }

    private void setEyeIcon(String imageName) {
        URL imageUrl = getClass().getResource("/images/" + imageName);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(24);
            imageView.setFitHeight(24);
            eyeLabel.setGraphic(imageView);
        } else {
            System.out.println("Image not found: " + imageName);
        }
    }
}
