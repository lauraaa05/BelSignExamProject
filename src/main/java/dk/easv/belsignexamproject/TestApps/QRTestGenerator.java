package dk.easv.belsignexamproject.TestApps;

import bll.QRCodeManager;
import dal.QRCodeDAO;

public class QRTestGenerator {
    public static void main(String[] args) {
        QRCodeManager qrService = new QRCodeManager();
        QRCodeDAO qrDao = new QRCodeDAO();

        int userId = 3;
        String qrContent = String.valueOf(userId);

        try {
            byte[] qrBytes = qrService.generateQRCodeAsBytes(qrContent);
            qrDao.saveQRCode(qrBytes, qrContent, userId);
            System.out.println("QR code saved to database for user ID: " + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}