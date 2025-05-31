package dk.easv.belsignexamproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestMainLogin extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/MainLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Main Log In");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}