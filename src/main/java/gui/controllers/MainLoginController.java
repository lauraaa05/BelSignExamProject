package gui.controllers;
import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import bll.LoginManager;
import dal.UserRole;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import utilities.LoggedInUser;
import utilities.SceneNavigator;

public class MainLoginController {

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
    private final SceneNavigator sceneNavigator = new SceneNavigator();

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

        User user = loginManager.login(username, password);

        if (user != null) {
            errorLabel.setVisible(false);
            LoggedInUser.setUser(user);
            Stage currentStage = (Stage) logInButton.getScene().getWindow();

            UserRole role = UserRole.fromString(user.getRole());

            switch (role) {
                case OPERATOR -> switchToOperatorMainScreen(currentStage, (Operator) user);
                case ADMIN -> switchToAdminMainScreen(currentStage, (Admin) user);
                case QUALITY_CONTROL -> switchToMainSceneSameWindow(currentStage, (QualityControl) user);
            }
        } else {
            errorLabel.setText("Incorrect username or password!");
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

    private void switchToMainSceneSameWindow(Stage currentStage, QualityControl qcu) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        QCUMainController qcumainController = fxmlLoader.getController();
        qcumainController.setLoggedInQCU(qcu);

        currentStage.setTitle("QCU Main");
        currentStage.setScene(scene);
        currentStage.show();
    }

    private void switchToAdminMainScreen(Stage currentStage, Admin admin) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AdminUserScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        AdminUserController adminUserController = fxmlLoader.getController();
        adminUserController.setLoggedInAdmin(admin);

        currentStage.setTitle("Admin User Management");
        currentStage.setScene(scene);
        currentStage.show();
    }

    private void switchToOperatorMainScreen(Stage currentStage, Operator operator) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OperatorMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        OperatorMainController operatorMainController = fxmlLoader.getController();
        operatorMainController.setLoggedInOperator(operator);

        currentStage.setTitle("Operator Main");
        currentStage.setScene(scene);
        currentStage.show();
    }

//    private void switchToOperatorMainScreen(ActionEvent event, Operator operator) throws IOException {
//        sceneNavigator.switchTo(event, "OperatorMain.fxml");
//    }

    // handle sign out
    @FXML
    private void handleSignOutButtonClick(ActionEvent event) {
        try {
            // Get current stage from the button source
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Load login screen again
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainLogin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            currentStage.setTitle("QCU Login");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actQRLogin(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "OperatorLogIn.fxml");
    }
}
