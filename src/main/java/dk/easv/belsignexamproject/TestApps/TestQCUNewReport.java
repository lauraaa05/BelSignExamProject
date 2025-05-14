package dk.easv.belsignexamproject.TestApps;

import dk.easv.belsignexamproject.OperatorLogInApp;
import gui.controllers.QCUNewReportController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class TestQCUNewReport extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCUNewReport.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        QCUNewReportController controller = fxmlLoader.getController();

        stage.setTitle("QCU Report Test");
        stage.setScene(scene);
        stage.show();

        javafx.application.Platform.runLater(() -> {
            controller.setOrderNumber("45-2025-04-028746");
        });
    }

    public static void main(String[] args) {
        launch();
    }

}
