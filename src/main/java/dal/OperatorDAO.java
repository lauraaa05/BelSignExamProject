package dal;


import be.Operator;

import java.sql.*;

public class OperatorDAO {

    private DatabaseMetaData DBConnector;

    public Operator getOperatorByToken(String qrToken) {
            try (Connection conn = DBConnector.getConnection()) {
                String sql = "SELECT * FROM Operator WHERE qr_token = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, qrToken);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Operator op = new Operator();
                    op.setId(rs.getInt("id"));
                    op.setName(rs.getString("name"));
                    op.setRole(rs.getString("role"));
                    op.setQrToken(rs.getString("qr_token"));
                    return op;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
