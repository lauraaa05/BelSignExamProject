package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDAO {

    private DBAccess dbAccess = new DBAccess();

    public boolean updateOrderStatusAndRole(String orderCode, String currentRole, String newRole, String newStatus) {
        String sql = """
            UPDATE OrderStatus 
            SET Role = ?, Status = ?, LastUpdated = GETDATE() 
            WHERE OrderCode = ? AND Role = ?
        """;

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newRole);
            stmt.setString(2, newStatus);
            stmt.setString(3, orderCode);
            stmt.setString(4, currentRole);

            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Version that returns OrderCodes
    public List<String> getRawOrderCodesByRoleAndStatus(String role, String status) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT OrderCode FROM OrderStatus WHERE Role = ? AND Status = ?";

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setString(2, status);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String orderCode = rs.getString("OrderCode");
                results.add(orderCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Version that returns formatted orders with details from the Orders table
    public List<String> getFormattedOrdersByRoleAndStatus(String role, String status) {
        List<String> orders = new ArrayList<>();
        String query = """
            SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode
            FROM Orders o
            INNER JOIN OrderStatus s ON o.OrderCode = s.OrderCode
            WHERE s.Role = ? AND s.Status = ?;
        """;

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                int year = rs.getInt("Year");
                String month = rs.getString("Month").trim();
                String orderCode = rs.getString("OrderCode");

                if (month.length() == 1) {
                    month = "0" + month;
                }

                String formattedOrder = countryNumber + "-" + year + "-" + month + "-" + orderCode;
                orders.add(formattedOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String getStatusForOrder(String orderNumber) {
        String sql = "SELECT Status FROM OrderStatus WHERE OrderCode = ?";

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateOrderStatus(String orderCode, String role, String newStatus) {
        String sql = "UPDATE OrderStatus SET Status = ?, LastUpdated = GETDATE() WHERE OrderCode = ? AND Role = ?";

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, orderCode);
            stmt.setString(3, role);

            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
