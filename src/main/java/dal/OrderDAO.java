package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<String> getFormattedOrderNumbers() {
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

                if (month.length() == 1) {
                    month = "0" + month;
                }

                String formattedOrder = countryNumber + "-" + year + "-" + month  + "-" + orderCode;
                orders.add(formattedOrder);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
