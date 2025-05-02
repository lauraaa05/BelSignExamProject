package dk.easv.belsignexamproject;

import bll.PictureManager;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import dal.PictureDAO;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bll.Camera;

public class PictureTest extends Application {

    private Camera camera;
    private ImageView imageView;
    private PictureManager pictureManager;

    @Override
    public void start(Stage primaryStage) {
        camera = new Camera();
        camera.initializeCamera();

        PictureDAO pictureDAO = new PictureDAO();
        pictureManager = new PictureManager(pictureDAO, camera); // Inject both camera and DAO

        imageView = new ImageView();
        Button captureButton = new Button("Capture & Save to DB");

        captureButton.setOnAction(e -> pictureManager.takeAndSavePicture());

        VBox root = new VBox(10, imageView, captureButton);
        Scene scene = new Scene(root, 640, 520);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Test");
        primaryStage.show();

        Thread webcamStream = new Thread(() -> {
            while (true) {
                BufferedImage grabbedImage = camera.takePicture();
                if (grabbedImage != null) {
                    javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(grabbedImage, null);
                    javafx.application.Platform.runLater(() -> imageView.setImage(fxImage));
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        webcamStream.setDaemon(true);
        webcamStream.start();
    }

    @Override
    public void stop() throws Exception {
        if (camera != null) {
            camera.closeCamera();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}