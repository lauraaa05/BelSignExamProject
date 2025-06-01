    package dal;

    import dal.interfaceDAO.IDoneReportDAO;
    import exceptions.DALException;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;

    public class DoneReportDAO implements IDoneReportDAO {

        public String getSignatureNameByOrderCode(String orderCode) throws DALException {
            String sql = "SELECT UL.FirstName, UL.LastName " +
                    "FROM DoneReport DR " +
                    "INNER JOIN UserLogin UL ON DR.SignedBy = UL.UserId " +
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
            } catch (SQLException e) {
                throw new DALException("Failed to fetch signature by order code: " + orderCode, e);
            }
            return "Unknown";
        }
    }
