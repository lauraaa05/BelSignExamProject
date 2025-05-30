package dal;

import be.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDAO {

    private DBAccess dbAccess = new DBAccess();

    public boolean updateOrderStatusAndRole(String orderCode, String currentRole, String newRole, String newStatus) {
        String sql = """
            UPDATE OrderStatusOrder 
            SET UserRole = ?, OrderStatus = ?, LastUpdated = GETDATE() 
            WHERE OrderCode = ? AND UserRole = ?
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
        String sql = "SELECT OrderCode " +
                "FROM OrderStatusOrder " +
                "JOIN OrderStatus s ON oso.OrderStatus = s.StatusId " +
                "WHERE oso.UserRole = ? AND s.Status = ?";

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

    public String getStatusForOrder(String orderNumber) {
        String sql = "SELECT s.Status " +
                "FROM OrderStatusOrder oso " +
                "JOIN OrderStatus s ON oso.OrderStatus = s.StatusId " +
                "WHERE oso.OrderCode = ?";

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

    public List<Order> getOrdersByRoleAndStatuses(String roleName, List<String> statuses) {
        List<Order> orders = new ArrayList<>();
        if (statuses == null || statuses.isEmpty()) {
            return orders;
        }

        int roleId = getRoleIdByName(roleName);

        String placeHolders = String.join(",",statuses.stream().map(s -> "?").toList());

        String query = """
                SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode, o.OrderGroupId
                FROM Orders o
                JOIN OrderStatusOrder oso ON o.OrderCode = oso.OrderCode
                JOIN  OrderStatus s ON oso.OrderStatus = s.StatusId
                WHERE oso.UserRole = ? AND s.Status IN (%s)
                """.formatted(placeHolders);

        try (Connection conn = dbAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roleId);
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
        String sql = "UPDATE OrderStatusOrder SET OrderStatus = ?, LastUpdated = GETDATE() WHERE OrderCode = ? AND UserRole = ?";

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

    private int getStatusIdByName(String statusName) throws SQLException {
        String sql = "SELECT StatusId FROM OrderStatus WHERE Status = ?";
        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("StatusId");
            } else {
                throw new SQLException("Status not found: " + statusName);
            }
        }
    }

    private int getRoleIdByName(String roleName) {
        String sql = "SELECT Id FROM UserRoles WHERE RoleName = ?";
        try (Connection conn = dbAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            } else  {
                throw new SQLException("No role found for id: " + roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}