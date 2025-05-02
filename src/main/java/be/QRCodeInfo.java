package be;

public class QRCodeInfo {
    private byte[] image;
    private String qrCodeString;
    private int userId;

    public QRCodeInfo(byte[] image, String qrCodeString, int userId) {
        this.image = image;
        this.qrCodeString = qrCodeString;
        this.userId = userId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getQrCodeString() {
        return qrCodeString;
    }

    public void setQrCodeString(String qrCodeString) {
        this.qrCodeString = qrCodeString;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}