package dal;


import be.Operator;

import java.sql.*;

public class OperatorDAO {

    private DBAccess dbAccess = new DBAccess();

    public Operator getOperatorById(int userId) {
        try (Connection conn = dbAccess.DBConnection()) {
            String sql = "SELECT * FROM UserLogin WHERE UserId = ? AND Role = 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Operator op = new Operator();
                op.setId(rs.getInt("UserId"));
                op.setName(rs.getString("Username"));
                op.setRole(rs.getString("Role"));
                op.setFirstName(rs.getString("FirstName"));
                return op;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}