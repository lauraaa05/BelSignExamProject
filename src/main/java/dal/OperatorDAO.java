package dal;


import be.Operator;

import java.sql.*;

public class OperatorDAO {

    private DBAccess dbAccess = new DBAccess();

    public Operator getOperatorById(int userId) {
        try (Connection conn = dbAccess.DBConnection()) {
            String sql = "SELECT * FROM LoginInfo WHERE UserId = ? AND Role = 'Operator'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Operator op = new Operator();
                op.setId(rs.getInt("UserId"));
                op.setName(rs.getString("Username"));
                op.setRole(rs.getString("Role"));
                return op;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
