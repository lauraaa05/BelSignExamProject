package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public boolean validateQualityControlUser(String username, String password) {
        String sql = "SELECT Role FROM LoginInfo WHERE Username = ? AND Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("Role");

                try {
                    UserRole role = UserRole.fromString(roleFromDB);
                    return role == UserRole.QUALITY_CONTROL;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean validateOperatorUser(String username, String password) {
        String sql = "SELECT Role FROM LoginInfo WHERE Username = ? AND Password = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleFromDB = rs.getString("Role");
                try {
                    UserRole role = UserRole.fromString(roleFromDB);
                    return role == UserRole.OPERATOR;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
