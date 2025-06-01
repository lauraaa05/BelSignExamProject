package dal.interfaceDAO;

import be.QRCodeInfo;

import java.sql.SQLException;

public interface IQRCodeDAO {

    void saveQRCode(byte[] imageBytes, String qrContent, int userId) throws SQLException;

    QRCodeInfo getLatestQRCode() throws SQLException;
}