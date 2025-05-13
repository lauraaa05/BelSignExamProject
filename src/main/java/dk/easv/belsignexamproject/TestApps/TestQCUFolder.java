package dk.easv.belsignexamproject.TestApps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TestQCUFolder extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/view/QCUFolderScreen.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find FXML file: /view/QCUFolderScreen.fxml");
        }
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("QCU Folder Screen");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}