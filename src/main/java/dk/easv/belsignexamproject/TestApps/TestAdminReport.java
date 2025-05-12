package dk.easv.belsignexamproject.TestApps;

import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestAdminReport extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/AdminReport.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Admin Report");
        stage.setScene(scene);
        stage.show();
    }
}
