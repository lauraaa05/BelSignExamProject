package dk.easv.belsignexamproject;

import bll.QRCodeService;
import dal.QRCodeDAO;

public class QRTestGenerator {
    public static void main(String[] args) {
        QRCodeService qrService = new QRCodeService();
        QRCodeDAO qrDao = new QRCodeDAO();

        int userId = 1;
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