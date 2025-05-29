package dal;


import be.Operator;

import java.sql.*;

public class OperatorDAO {

    private DBAccess dbAccess = new DBAccess();

    public Operator getOperatorById(int userId) {
        String sql = "SELECT * FROM UserLogin ul JOIN UserRoles ur ON ul.Role = ur.Id WHERE ul.UserId = ? AND ur.RoleName = 'Operator'";

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String email = rs.getString("Email");

                Operator op = new Operator(userId, username, "Operator", null, firstName, lastName);
                op.setPassword(password);
                op.setEmail(email);
                return op;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}