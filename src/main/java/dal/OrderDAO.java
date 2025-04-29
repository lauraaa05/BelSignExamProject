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
/*
        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int countryNumber = rs.getInt("CountryNumber");
                int year = rs.getInt("Year");
                int month = rs.getInt("Month");
            }

        }*/
        return null;
    }
}
