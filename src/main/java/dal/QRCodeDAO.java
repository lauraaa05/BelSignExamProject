package dal;

import be.QRCodeInfo;
import com.google.zxing.qrcode.encoder.QRCode;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QRCodeDAO {

    DBAccess db = new DBAccess();

    public void saveQRCode(byte[] imageBytes, String qrContent, int userId) throws SQLException {
        String sql = "INSERT INTO QRCode (QRCodeImage, QRCodeString, UserId) VALUES (?, ?, ?)";

        try (Connection conn = db.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBytes(1, imageBytes);
            stmt.setString(2, qrContent);
            stmt.setInt(3, userId);

            stmt.executeUpdate();
        }
    }

    public QRCodeInfo getQRCodeByString(String qrString) throws SQLException {
        String sql = "SELECT * FROM QRCode WHERE QRCodeString = ?";

        try (Connection conn = db.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, qrString);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] image = rs.getBytes("QRCodeImage");
                String qrCodeString = rs.getString("QRCodeString");
                int userId = rs.getInt("UserId");

                return new QRCodeInfo(image, qrCodeString, userId);
            }
        }
        return null;
    }

    public QRCodeInfo getLatestQRCode() throws SQLException {
        String sql = "SELECT TOP 1 * FROM QRCode ORDER BY QRCodeId DESC";

        try (Connection conn = db.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                byte[] image = rs.getBytes("QRCodeImage");
                String qrCodeString = rs.getString("QRCodeString");
                int userId = rs.getInt("UserId");

                return new QRCodeInfo(image, qrCodeString, userId);
            }
        }
        return null;
    }
}