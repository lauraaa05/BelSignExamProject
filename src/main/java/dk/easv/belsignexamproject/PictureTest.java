package dk.easv.belsignexamproject;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
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

import bll.Camera; // your Camera class from bll

public class PictureTest extends Application {

    private Camera camera; // Renamed from cameraService
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        camera = new Camera();
        camera.initializeCamera(); // Open webcam through Camera

        imageView = new ImageView();
        Button captureButton = new Button("Capture Photo");

        captureButton.setOnAction(e -> captureImage());

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

    private void captureImage() {
        BufferedImage image = camera.takePicture();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss"));
        String fileName = "src/main/resources/images/photo_" + timestamp + ".png";
        try {
            ImageIO.write(image, "PNG", new File(fileName));
            System.out.println("Picture taken!");
        } catch (IOException e) {
            e.printStackTrace();
        }
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