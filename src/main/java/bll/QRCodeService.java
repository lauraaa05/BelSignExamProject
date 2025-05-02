package bll;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Service for generating and validating QR Codes.
 * Supports both file and byte array operations.
 */
public class QRCodeService {


    public String generateQRCodeImage(String token, String fileName) throws WriterException, IOException {
        int width = 250;
        int height = 250;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, width, height);

        String directory = "qrcodes/";
        Path path = FileSystems.getDefault().getPath(directory + fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return path.toString();
    }


    public String readQRCode(String filePath) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }


    public boolean isValidQRCode(String filePath, String expectedToken) {
        try {
            String content = readQRCode(filePath);
            return content.equals(expectedToken);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generates a QR code and returns it as a byte array (e.g., for database storage).
     */
    public byte[] generateQRCodeAsBytes(String token) throws WriterException, IOException {
        int width = 250;
        int height = 250;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }


    public String readQRCodeFromBytes(byte[] imageBytes) throws IOException, NotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    /**
     * Validates QR code content from image bytes against the expected token.
     *
     * @param imageBytes     PNG data
     * @param expectedToken  token to validate against
     * @return true if valid, false if not
     */
    public boolean isValidQRCode(byte[] imageBytes, String expectedToken) {
        try {
            String content = readQRCodeFromBytes(imageBytes);
            return content.equals(expectedToken);
        } catch (Exception e) {
            return false;
        }
    }

    public String readQRCodeFromImage(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }
}
