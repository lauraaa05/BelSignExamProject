package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDAO {

    private DBAccess dbAccess = new DBAccess();

    public List<String> getFormattedOrdersByRoleAndStatus(String role, String status) {
        List<String> orders = new ArrayList<>();
        String query = """
                SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode FROM Orders o
                INNER JOIN OrderStatus s ON o.OrderCode = s.OrderCode
                WHERE s.Role = ? AND s.Status = ?;""";

        try (Connection conn = new DBAccess().DBConnection();
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

                String formattedOrder = countryNumber + "-" + year + "-" + month  + "-" + orderCode;
                orders.add(formattedOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateOrderStatus(String orderCode, String role, String newStatus) {
        String sql = "UPDATE OrderStatus SET Status = ?, LastUpdated = GETDATE() WHERE OrderCode = ? AND Role = ?";

        try (Connection conn = dbAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, orderCode);
            stmt.setString(3, role);
            System.out.println("Trying to update order: " + orderCode + ", role: " + role + ", new status: " + newStatus);
            int rows =  stmt.executeUpdate();

            System.out.println("Updated rows: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getToDOOrders(String orderCode, String role, String status) {
        List<String> toDoOrders = new ArrayList<>();
        String query = """
                SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode FROM Orders o
                INNER JOIN OrderStatus s ON o.OrderCode = s.OrderCode
                WHERE s.Role = ? AND s.Status = ?;""";
    }
}
