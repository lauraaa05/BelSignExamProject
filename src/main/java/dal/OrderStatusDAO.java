package dal;

import be.Order;

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

//    public List<Order> getOrdersByRoleAndStatus(String role, String status) {
//        List<Order> orders = new ArrayList<>();
//        String query = """
//            SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode, o.OrderGroupId
//            FROM Orders o
//            INNER JOIN OrderStatus s ON o.OrderCode = s.OrderCode
//            WHERE s.Role = ? AND s.Status = ?;
//        """;
//
//        try (Connection conn = dbAccess.DBConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, role);
//            stmt.setString(2, status);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                int countryNumber = rs.getInt("CountryNumber");
//                int year = rs.getInt("Year");
//                String month = rs.getString("Month");
//                String orderCode = rs.getString("OrderCode");
//                int orderGroupId = rs.getInt("OrderGroupId");
//
//                orders.add(new Order(countryNumber, year, month, orderCode, orderGroupId));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orders;
//    }
//
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

    public List<Order> getOrdersByRoleAndStatuses(String role, List<String> statuses) {
        List<Order> orders = new ArrayList<>();
        if (statuses == null || statuses.isEmpty()) {
            return orders;
        }

        String placeHolders = String.join(",",statuses.stream().map(s -> "?").toList());

        String query = """
                SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode, o.OrderGroupId
                FROM Orders o
                INNER JOIN OrderStatus s ON o.OrderCode = s.OrderCode
                WHERE s.Role = ? AND s.Status IN (%s)
                """.formatted(placeHolders);

        try (Connection conn = dbAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role);
            for (int i = 0; i < statuses.size(); i++) {
                stmt.setString(i + 2, statuses.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                int year = rs.getInt("Year");
                String month = rs.getString("Month");
                String orderCode = rs.getString("OrderCode");
                int orderGroupId = rs.getInt("OrderGroupId");

                orders.add(new Order(countryNumber, year, month, orderCode, orderGroupId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
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

    public boolean saveStatusForOrder(String orderCode, String role, String status) {
        String sql = "MERGE INTO OrderStatus AS target " +
                    "USING (SELECT ? AS orderCode, ? AS role) AS source " +
                    "ON target.orderCode = source.orderCode AND target.role = source.role " +
                    "WHEN MATCHED THEN UPDATE SET status = ? " +
                    "WHEN NOT MATCHED THEN INSERT (orderCode, role, status) VALUES (?, ?, ?);";

        try (Connection conn = DBAccess.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderCode);
            stmt.setString(2, role);
            stmt.setString(3, status);
            stmt.setString(4, orderCode);
            stmt.setString(5, role);
            stmt.setString(6, status);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}