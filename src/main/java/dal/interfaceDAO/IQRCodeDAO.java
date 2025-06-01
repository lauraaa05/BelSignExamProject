package dal.interfaceDAO;

import be.QRCodeInfo;
import exceptions.DALException;

import java.sql.SQLException;

public interface IQRCodeDAO {

    void saveQRCode(byte[] imageBytes, String qrContent, int userId) throws DALException;

    QRCodeInfo getLatestQRCode() throws DALException;
}