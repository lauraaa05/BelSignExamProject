package bll;

import be.Picture;
import dal.PictureDAO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PictureManager {

    private final PictureDAO pictureDAO;

    public PictureManager(PictureDAO pictureDAO) {
        this.pictureDAO = pictureDAO;
    }

    public void savePictureToDB(BufferedImage image, String orderNumber) throws IOException, SQLException {
        System.out.println("Saving picture for order " + orderNumber);
        System.out.println("Image is null= " + (image == null));
        System.out.println("Order number is null? " + (orderNumber == null));

        String fileName = generateTimestampedFileName();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        Picture picture = new Picture(imageBytes, fileName, LocalDateTime.now(), orderNumber);
        pictureDAO.savePicture(picture);
    }

    private String generateTimestampedFileName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return  LocalDateTime.now().format(dtf) + ".png";
    }
}