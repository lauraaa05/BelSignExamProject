package dal;

import be.OrderNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Helper: Format OrderNumber as "CountryNumber-OrderCode"
    private static String formatOrderNumber(int countryNumber, String orderCode) {
        return countryNumber + "-" + orderCode.trim();
    }

    // Get all formatted order numbers
    public static List<String> getFormattedOrderNumbers() {
        List<String> orderNumbers = new ArrayList<>();
        String sql = "SELECT CountryNumber, OrderCode FROM Orders";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                String orderCode = rs.getString("OrderCode");

                orderNumbers.add(formatOrderNumber(countryNumber, orderCode));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching formatted orders: " + e.getMessage());
        }

        return orderNumbers;
    }

    // Search formatted orders by CountryNumber, OrderCode, or Year
    public static List<String> searchFormattedOrders(String keyword) {
        List<String> matchedOrders = new ArrayList<>();
        String sql = """
            SELECT CountryNumber, OrderCode FROM Orders 
            WHERE CAST(CountryNumber AS NVARCHAR) LIKE ? 
               OR LTRIM(RTRIM(OrderCode)) LIKE ? 
               OR CAST(Year AS NVARCHAR) LIKE ?
            """;

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String wildcard = "%" + keyword.trim() + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                String orderCode = rs.getString("OrderCode");

                matchedOrders.add(formatOrderNumber(countryNumber, orderCode));
            }
        } catch (SQLException e) {
            System.err.println("Error during order search: " + e.getMessage());
        }

        return matchedOrders;
    }

    // Check if an order code exists
    public boolean orderCodeExists(String orderCode) {
        String sql = "SELECT 1 FROM Orders WHERE LTRIM(RTRIM(OrderCode)) = ?";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderCode.trim());
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking order code existence: " + e.getMessage());
        }

        return false;
    }

    // Get OrderNumbers by Year and Month
    public List<OrderNumber> getOrderNumbersByFolder(String year, String month) {
        List<OrderNumber> results = new ArrayList<>();
        String sql = "SELECT OrderNumber, status, date FROM OrderNumbersTable WHERE Year = ? AND Month = ?";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, year);
            stmt.setString(2, month);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderNumber order = new OrderNumber(
                        rs.getInt("OrderNumber"),
                        rs.getString("status"),
                        rs.getString("date")
                );
                results.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving OrderNumbers: " + e.getMessage());
        }

        return results;
    }
}
