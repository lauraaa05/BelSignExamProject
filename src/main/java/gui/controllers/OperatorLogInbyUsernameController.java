package gui.controllers;

import bll.LoginManager;
import gui.model.LoginModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class OperatorLogInbyUsernameController {

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

    @FXML
    private MFXButton logInButton;

    private boolean passwordVisible = false;

    private final LoginManager loginManager = new LoginManager();
    private final LoginModel loginModel = new LoginModel();

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);

        passwordFieldMasked.setVisible(true);
        passwordFieldVisible.setVisible(false);

        setEyeIcon("eyeOpened.png");

        logInButton.setOnAction(_ -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        eyeLabel.setOnMouseClicked(e -> togglePasswordVisibility());

        usernameField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                passwordFieldMasked.requestFocus();
            }
        });

        passwordFieldMasked.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                try {
                    handleLogin();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordVisible
                ? passwordFieldVisible.getText().trim()
                : passwordFieldMasked.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(true);
            return;
        }

        boolean isValid = loginModel.loginAsOperator(username, password);

        if (isValid) {
            errorLabel.setVisible(false);
            Stage currentStage = (Stage) logInButton.getScene().getWindow();
            switchToMainSceneSameWindow(currentStage);
        } else {
            errorLabel.setText("Incorrect username or password for Quality Control");
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("QCU Main");
        currentStage.setScene(scene);
        currentStage.show();
    }

}
