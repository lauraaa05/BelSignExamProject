package dal;

import be.Picture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PictureDAO {

    DBAccess db = new DBAccess();

    public void savePicture(Picture picture) throws SQLException {
        String sql = "INSERT INTO Pictures (Image, Timestamp, FileName, OrderNumber) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.DBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, picture.getImage());
            pstmt.setTimestamp(2, Timestamp.valueOf(picture.getTimestamp()));
            pstmt.setString(3, picture.getFileName());
            pstmt.setString(4, picture.getOrderNumber());

            pstmt.executeUpdate();
        }
    }

    public List<Picture> getPicturesByOrderNumber(String orderNumber) throws SQLException {
        List<Picture> pictures = new ArrayList<>();
        String sql = "SELECT Image, FileName, Timestamp, OrderNumber FROM Pictures WHERE OrderNumber = ?";

        try (Connection conn = db.DBConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                byte[] imageBytes = rs.getBytes("Image");
                String fileName = rs.getString("FileName");
                Timestamp timestamp = rs.getTimestamp("Timestamp");
                String dbOrderNumber = rs.getString("OrderNumber");

                Picture picture = new Picture(imageBytes, fileName, timestamp.toLocalDateTime(), dbOrderNumber);
                pictures.add(picture);
            }
        }
        return pictures;
    }
}