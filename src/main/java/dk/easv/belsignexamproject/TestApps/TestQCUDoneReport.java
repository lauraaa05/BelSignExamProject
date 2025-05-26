package dk.easv.belsignexamproject.TestApps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestQCUDoneReport extends Application {


        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QCUDoneReport.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("QCUDoneReport");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
