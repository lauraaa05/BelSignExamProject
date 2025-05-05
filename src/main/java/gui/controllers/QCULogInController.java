package gui.controllers;

import bll.LoginManager;
import dk.easv.belsignexamproject.OperatorLogInApp;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
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

        logInButton.setOnAction(e -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        eyeLabel.setOnMouseClicked(e -> togglePasswordVisibility());
    }

    private void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordVisible
                ? passwordFieldVisible.getText().trim()
                : passwordFieldMasked.getText().trim();

        boolean isValid = loginManager.checkQCULogin(username, password);

        if (isValid) {
            errorLabel.setVisible(false);
            //Opens the QCUMain
            Stage currentStage = (Stage) logInButton.getScene().getWindow();
            switchToMainSceneSameWindow(currentStage);
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

    private void switchToMainSceneSameWindow(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCUMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("QCU Main");
        currentStage.setScene(scene);
        currentStage.show();  // Not strictly necessary (stage is already showing), but safe to call
    }
}
