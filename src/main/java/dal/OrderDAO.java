package dal;

import be.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderDAO {

    public OrderDAO() {
    }

    public static List<Order> getFormattedOrderNumbers() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT CountryNumber, Year, Month, OrderCode, OrderGroupId FROM Orders";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                int year = rs.getInt("Year");
                String month = rs.getString("Month");
                String orderCode = rs.getString("OrderCode");
                int orderGroupId = rs.getInt("OrderGroupId");

                orders.add(new Order(countryNumber, year, month, orderCode, orderGroupId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.sort(Comparator.comparingInt(Order::getOrderGroupId));

        return orders;
    }

    public List<Order> searchFormattedOrders(String searchTerm) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT CountryNumber, Year, Month, OrderCode, OrderGroupId FROM Orders " +
                "WHERE CAST(CountryNumber AS TEXT) LIKE ? OR " +
                "CAST(Year AS TEXT) LIKE ? OR " +
                "Month LIKE ? OR " +
                "OrderCode LIKE ?";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String pattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, pattern);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("CountryNumber"),
                        rs.getInt("Year"),
                        rs.getString("Month"),
                        rs.getString("OrderCode"),
                        rs.getInt("OrderGroupId")
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<OrderDAO> searchByMonthYearOrOrder(String query) {
        List<OrderDAO> results = new ArrayList<>();
        String sql = "SELECT * FROM folders WHERE order_number LIKE ? OR month LIKE ? OR year LIKE ?";

        try (Connection conn = DBAccess.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + query + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDAO folder = new OrderDAO();
                results.add(folder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    public List<Order> searchOrdersByYear(int year) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT CountryNumber, Year, Month, OrderCode FROM Orders WHERE Year = ?";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);  // Set the year to filter the orders

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                            rs.getInt("CountryNumber"),
                            year,
                            rs.getString("Month"),
                            rs.getString("OrderCode"),
                            rs.getInt("OrderGroupId")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    private void add(OrderDAO folder) {
    }

    public List<Order> getOrdersForDate(int year, int month) {
        List<Order> filteredOrders = new ArrayList<>();
        String query = "SELECT CountryNumber, Year, Month, OrderCode FROM Orders WHERE Year = ? AND Month = ?";

        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);
            stmt.setString(2, String.format("%02d", month));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    filteredOrders.add(new Order(
                            rs.getInt("CountryNumber"),
                            year,
                            rs.getString("Month"),
                            rs.getString("OrderCode"),
                            rs.getInt("OrderGroupId")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredOrders;
    }
}