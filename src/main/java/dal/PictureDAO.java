package dal;

import be.Picture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class PictureDAO {

    DBAccess db = new DBAccess();

    public void savePicture(Picture picture) throws Exception {
        String sql = "INSERT INTO Pictures (Image, Timestamp, Filename) VALUES (?, ?, ?)";

        try (Connection conn = db.DBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, picture.getImage());
            pstmt.setTimestamp(2, Timestamp.valueOf(picture.getTimestamp()));
            pstmt.setString(3, picture.getFileName());

            pstmt.executeUpdate();
        }
    }
}