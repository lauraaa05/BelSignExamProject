package be;

import java.time.LocalDateTime;

public class Picture {

    private int imageId;
    private byte[] image;
    private LocalDateTime timestamp;
    private String side;
    private String fileName;
    private String orderNumber;

    public Picture(int imageId, byte[] image, LocalDateTime timestamp, String side, String fileName, String orderNumber) {
        this.imageId = imageId;
        this.image = image;
        this.timestamp = timestamp;
        this.side = side;
        this.fileName = fileName;
        this.orderNumber = orderNumber;
    }

    public Picture(byte[] image, LocalDateTime timestamp, String side, String orderNumber) {
        this.image = image;
        this.timestamp = timestamp;
        this.side = side;
        this.orderNumber = orderNumber;
    }

    public Picture(byte[] imageBytes, String fileName, LocalDateTime timestamp, String orderNumber) {
        this.image = imageBytes;
        this.fileName = fileName;
        this.timestamp = timestamp;
        this.orderNumber = orderNumber;
        this.side = ""; // Optional fallback if you don't know side here
    }

    public Picture(byte[] imageBytes, String fileName, LocalDateTime timestamp, String orderNumber, String side) {
        this.image = imageBytes;
        this.fileName = fileName;
        this.timestamp = timestamp;
        this.orderNumber = orderNumber;
        this.side = side;
    }

    public Picture(byte[] image, LocalDateTime timestamp, String side) {
        this.image = image;
        this.timestamp = timestamp;
        this.side = side;
        this.fileName = "";
        this.orderNumber = "";
    }

    public int getImageId() {
        return imageId;
    }

    public byte[] getImage() {
        return image;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSide() {
        return side;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setSide(String side) {
        this.side = side;
    }
}