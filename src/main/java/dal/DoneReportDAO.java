    package dal;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;

    public class DoneReportDAO {

        public String getSignatureNameByOrderCode(String orderCode) throws SQLException {
            String sql = "SELECT LI.FirstName, LI.LastName " +
                    "FROM DoneReport DR " +
                    "INNER JOIN LoginInfo LI ON DR.SignedBy = LI.UserId " +
                    "WHERE DR.OrderCode = ?";
            try (Connection conn = DBAccess.DBConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, orderCode);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    return firstName + " " + lastName;
                }
            }

            return "Unknown";
        }
    }
