package bll;

import be.Picture;
import dal.PictureDAO;
import exceptions.BLLException;
import exceptions.DALException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PictureManager {

    private final PictureDAO pictureDAO =  new PictureDAO();

    public void savePictureToDB(BufferedImage image, String orderNumber, LocalDateTime timestamp, String side) throws BLLException {
        System.out.println("Saving picture for order " + orderNumber);
        System.out.println("Image is null= " + (image == null));
        System.out.println("Order number is null? " + (orderNumber == null));

        try {
            String fileName = generateTimestampedFileName();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            Picture picture = new Picture(imageBytes, fileName, timestamp, orderNumber, side);
            pictureDAO.savePicture(picture);
        } catch (IOException e) {
            throw new BLLException("Failed to convert image to bytes.", e);
        } catch (DALException e) {
            throw new BLLException("Failed to save picture to database.", e);
        }
    }

    private String generateTimestampedFileName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return  LocalDateTime.now().format(dtf) + ".png";
    }

    public void deletePictureFromDB(int imageId) throws BLLException {
        try {
            pictureDAO.deletePictureById(imageId);
        } catch (DALException e) {
            throw new BLLException("Failed to delete picture from database.", e);
        }
    }

    public List<Picture> getPicturesByOrderNumber(String orderNumber) throws BLLException {
        try {
            return pictureDAO.getPicturesByOrderNumber(orderNumber);
        } catch (DALException e) {
            throw new BLLException("Failed to load pictures", e);
        }
    }

    public List<String> getTakenSidesForOrderNumber(String orderNumber) throws BLLException {
        try {
            return pictureDAO.getTakenSidesForOrderNumber(orderNumber);
        } catch (DALException e) {
            throw new BLLException("Failed to get taken sides", e);
        }
    }

    public int countImagesForOrderNumber(String orderNumber) throws BLLException {
        try {
            return pictureDAO.countImagesForOrderNumber(orderNumber);
        } catch (DALException e) {
            throw new BLLException("Failed to count pictures", e);
        }
    }

    public List<Picture> getPicturesByOrderNumberRaw(String orderNumber) throws DALException {
        return pictureDAO.getPicturesByOrderNumberRaw(orderNumber);
    }
}