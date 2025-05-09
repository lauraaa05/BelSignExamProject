package dk.easv.belsignexamproject.TestApps;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class LoadPictureTest extends Application {

    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        imageView = new ImageView();

        Button loadButton = new Button("Load Latest From DB");
        loadButton.setOnAction(e -> loadLatestPicture());

        VBox root = new VBox(10, imageView, loadButton);
        Scene scene = new Scene(root, 640, 520);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Load Picture From Database");
        primaryStage.show();
    }

    private void loadLatestPicture() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlserver://10.176.111.34;database=Belsign_Exam_LEEK;encrypt=true;trustServerCertificate=true",
                    "CSe2024b_e_15",
                    "CSe2024bE15!24"
            );

            String sql = "SELECT TOP 1 * FROM Pictures ORDER BY ImageId DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("Image");
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                Image fxImage = new Image(bais);
                imageView.setImage(fxImage);
                System.out.println("Loaded image: " + rs.getString("Filename"));
            } else {
                System.out.println("No image found.");
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}