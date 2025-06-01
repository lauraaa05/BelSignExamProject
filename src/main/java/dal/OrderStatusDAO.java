package dal;

import be.Order;
import dal.interfaceDAO.IOrderStatusDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDAO implements IOrderStatusDAO {

    private DBAccess dbAccess = new DBAccess();

    public boolean updateOrderStatusAndRole(String orderCode, int currentRoleId, int newRoleId, int newStatusId) throws DALException {
        String sql = """
            UPDATE OrderStatusOrder 
            SET UserRole = ?, OrderStatus = ?, LastUpdated = GETDATE() 
            WHERE OrderCode = ? AND UserRole = ?
        """;

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newRoleId);
            stmt.setInt(2, newStatusId);
            stmt.setString(3, orderCode);
            stmt.setInt(4, currentRoleId);

            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            throw new DALException("Failed to update order status and role for order: " + orderCode, e);
        }
    }

    public String getStatusForOrder(String orderNumber) throws DALException {
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
            throw new DALException("Failed to get status for order: " + orderNumber, e);
        }
        return null;
    }

    public List<Order> getOrdersByRoleAndStatuses(String roleName, List<String> statuses) throws DALException {
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
            throw new DALException("Failed to get orders for role: " + roleName, e);
        }
        return orders;
    }

    public boolean updateOrderStatus(String orderCode, int roleId, int newStatusId) throws DALException {
        String sql = "UPDATE OrderStatusOrder SET OrderStatus = ?, LastUpdated = GETDATE() WHERE OrderCode = ? AND UserRole = ?";

        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newStatusId);
            stmt.setString(2, orderCode);
            stmt.setInt(3, roleId);

            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            throw new DALException("Failed to update order status for order: " + orderCode, e);
        }
    }

    public int getStatusIdByName(String statusName) throws DALException {
        String sql = "SELECT StatusId FROM OrderStatus WHERE Status = ?";
        try (Connection conn = dbAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("StatusId");
            } else {
                throw new DALException("Status not found: " + statusName);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to fetch status id by name: " + statusName, e);
        }
    }

    private int getRoleIdByName(String roleName) throws DALException {
        String sql = "SELECT Id FROM UserRoles WHERE RoleName = ?";
        try (Connection conn = dbAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id");
            } else  {
                throw new DALException("No role found with name: " + roleName);
            }
        } catch (SQLException e) {
            throw new DALException("Failed to fetch role id by name: " + roleName, e);
        }
    }
}