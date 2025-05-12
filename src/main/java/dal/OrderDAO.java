package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderDAO {

    public OrderDAO() {
    }

    private static String formatOrderData(int countryNumber, int year, String month, String orderCode) {
        if (month.length() == 1) {
            month = "0" + month;
        }
        return countryNumber + "-" + year + "-" + month + "-" + orderCode;
    }

    public static List<String> getFormattedOrderNumbers() {
        List<String> orders = new ArrayList<>();
        String query = "SELECT CountryNumber, Year, Month, OrderCode FROM Orders";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                int year = rs.getInt("Year");
                String month = rs.getString("Month").trim();
                String orderCode = rs.getString("OrderCode");

                orders.add(formatOrderData(countryNumber, year, month, orderCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<String> searchFormattedOrders(String searchTerm) {
        List<String> orders = new ArrayList<>();
        String query = "SELECT CountryNumber, Year, Month, OrderCode FROM Orders " +
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
                int countryNumber = rs.getInt("CountryNumber");
                int year = rs.getInt("Year");
                String month = rs.getString("Month").trim();
                String orderCode = rs.getString("OrderCode");

                orders.add(formatOrderData(countryNumber, year, month, orderCode));
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

    private void add(OrderDAO folder) {
    }
}
