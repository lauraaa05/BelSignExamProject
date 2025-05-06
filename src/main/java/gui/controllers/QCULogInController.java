package gui.controllers;
import bll.LoginManager;
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
import javafx.event.ActionEvent;





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

    @FXML
    private MFXButton logInButton;

    private boolean passwordVisible = false;

    private final LoginManager loginManager = new LoginManager();

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

        boolean isValid = loginManager.checkQCULogin(username, password);

//        QCUMainController qcuMainController = fxmlLoader.getController();
//        qcuMainController.setLoggedInQCU(qcu);

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        currentStage.setTitle("QCU Main");
        currentStage.setScene(scene);
        currentStage.show();
    }

    // handle sign out
    @FXML
    private void handleSignOutButtonClick(ActionEvent event) {
        try {
            // Get current stage from the button source
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Load login screen again
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCULogIn.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            currentStage.setTitle("QCU Login");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
