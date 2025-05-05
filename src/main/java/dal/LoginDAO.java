package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    private DBAccess dbAccess = new DBAccess();

    public boolean validateQualityControlUser(String username, String password) {
        String sql = "SELECT Role FROM LoginInfo WHERE Username = ? AND Password = ?";

        try (Connection conn = dbAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("Role");

                //to check if the role matches with the Quality Control in Enum
                try {
                    UserRole role = UserRole.fromString(roleFromDB);
                    return role == UserRole.QUALITY_CONTROL;
                } catch (IllegalArgumentException e) {
                    //Invalid role
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

}
