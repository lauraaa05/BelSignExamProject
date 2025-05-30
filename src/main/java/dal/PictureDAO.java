package dal;

import be.Picture;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PictureDAO {

    DBAccess db = new DBAccess();

    public void savePicture(Picture picture) throws DALException {
        String sql = "INSERT INTO Pictures (Image, Timestamp, FileName, OrderNumber, Side) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = db.DBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, picture.getImageBytes());
            pstmt.setTimestamp(2, Timestamp.valueOf(picture.getTimestamp()));
            pstmt.setString(3, picture.getFileName());
            pstmt.setString(4, picture.getOrderNumber());
            pstmt.setString(5, formatSide(picture.getSide()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException("Failed to save picture to database", e);
        }
    }

    public List<Picture> getPicturesByOrderNumber(String orderNumber) throws DALException {
        List<Picture> pictures = new ArrayList<>();
        String sql = "SELECT ImageId, Image, FileName, Timestamp, OrderNumber, Side FROM Pictures WHERE OrderNumber = ?";

        try (Connection conn = db.DBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int imageId = rs.getInt("ImageId");
                byte[] imageBytes = rs.getBytes("Image");
                String fileName = rs.getString("FileName");
                Timestamp timestamp = rs.getTimestamp("Timestamp");
                String dbOrderNumber = rs.getString("OrderNumber");
                String dbSide = rs.getString("Side");

                Picture picture = new Picture();
                picture.setImageId(imageId);
                picture.setImage(imageBytes);
                picture.setFileName(fileName);
                picture.setTimestamp(timestamp.toLocalDateTime());
                picture.setSide(dbSide);
                picture.setOrderNumber(dbOrderNumber);
                pictures.add(picture);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve pictures from database", e);
        }
        return pictures;
    }

    public int countImagesForOrderNumber(String orderNumber) throws DALException {
        String sql = "SELECT COUNT(*) FROM Pictures WHERE OrderNumber = ?";
        try (Connection conn = db.DBConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to count images for order number", e);
        }
        return 0;
    }

    public List<String> getTakenSidesForOrderNumber(String orderNumber) throws DALException {
        List<String> sides = new ArrayList<>();

        String sql = "SELECT DISTINCT Side FROM Pictures WHERE OrderNumber = ?";
        try (Connection conn = db.DBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String side = rs.getString("Side");
                System.out.println("DAO fetched side: " + side);
                sides.add(side);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve taken sides for order number", e);
        }
        return sides;
    }

    //This is for retrieving images to QCU report
    public List<Picture> getPicturesByOrderNumberRaw(String orderNumber) throws DALException {
        List<Picture> pictures = new ArrayList<>();
        String sql = "SELECT ImageId, Image, Side, Timestamp, OrderNumber FROM Pictures WHERE OrderNumber = ?";

        try (Connection conn = db.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,orderNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int  imageId = rs.getInt("ImageId");
                byte[] imageBytes = rs.getBytes("Image");
                String side = rs.getString("Side");
                Timestamp timestamp = rs.getTimestamp("Timestamp");
                String dbOrderNumber = rs.getString("OrderNumber");

                Picture picture = new Picture(imageId, imageBytes, timestamp.toLocalDateTime(), side, dbOrderNumber);
                pictures.add(picture);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve pictures from database", e);
        }
        return pictures;
    }

    public void deletePictureById(int imageId) throws DALException {
        String sql = "DELETE FROM Pictures WHERE ImageId = ?";
        try (Connection conn = db.DBConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, imageId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException("Failed to delete picture from database with ID: " + imageId, e);
        }
    }

    private String formatSide(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}