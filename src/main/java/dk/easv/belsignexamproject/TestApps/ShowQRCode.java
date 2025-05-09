package dk.easv.belsignexamproject.TestApps;

import be.QRCodeInfo;
import dal.QRCodeDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class ShowQRCode extends Application {

    @Override
    public void start(Stage primaryStage) {
        QRCodeDAO qrDao = new QRCodeDAO();

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        try {
            QRCodeInfo qr = qrDao.getLatestQRCode();
            if (qr != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(qr.getImage());
                Image fxImage = new Image(bais);
                imageView.setImage(fxImage);
            } else {
                System.out.println("No QR code found in database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 350, 350);

        primaryStage.setTitle("View QR Code from DB");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}