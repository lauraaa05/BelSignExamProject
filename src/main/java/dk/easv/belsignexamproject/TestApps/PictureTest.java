package dk.easv.belsignexamproject.TestApps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import bll.CameraManager; // your Camera class from bll

public class PictureTest extends Application {

    private CameraManager camera = new CameraManager(); // Renamed from cameraService
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PictureView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Taking");
        primaryStage.show();
    }
}