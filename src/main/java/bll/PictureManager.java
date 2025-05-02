package bll;

import be.Picture;
import dal.PictureDAO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PictureManager {

    private final PictureDAO pictureDAO;
    private final Camera camera;

    public PictureManager(PictureDAO pictureDAO, Camera camera) {
        this.pictureDAO = pictureDAO;
        this.camera = camera;
    }

    public void takeAndSavePicture() {
        try {
            BufferedImage image = camera.takePicture();

            if (image == null) {
                System.out.println("No picture taken");
                return;
            }

            byte[] imageBytes = convertToBytes(image);

            String filename = "photo_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss")) + ".png";
            LocalDateTime timestamp = LocalDateTime.now();

            Picture picture = new Picture(0, imageBytes, timestamp, filename, 0);
            pictureDAO.savePicture(picture);

            System.out.println("Picture saved: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] convertToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }
}