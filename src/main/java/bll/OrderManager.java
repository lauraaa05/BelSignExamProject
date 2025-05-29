package bll;
import be.Order;
import dal.DBAccess;
import dal.OrderDAO;
import dal.interfaceDAO.IOrderDAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private OrderDAO od = new OrderDAO();
//    private IOrderDAO od = new OrderDAO();    you should use it like this

    public List<Order> getOrderNumbersAsList() {
        return od.getFormattedOrderNumbers();
    }

    public List<File> getOrderImages(String orderNumber) {
        return List.of();
    }

    public List<Order> getOrdersForDate(int year, int month) {
        List<Order> orders = new ArrayList<>();

        String sql = """
    SELECT o.CountryNumber, o.Year, o.Month, o.OrderCode, o.OrderGroupId
    FROM Orders o
    INNER JOIN OrderStatusOrder sso ON o.OrderCode = sso.OrderCode
    INNER JOIN OrderStatus st ON sso.OrderStatus = st.StatusId
    WHERE sso.UserRole = '2' AND st.Status = 'done'
    AND o.Year = ? AND o.Month = ?
""";

        try (Connection conn = new DBAccess().DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int country = rs.getInt("CountryNumber");
                int y = rs.getInt("Year");
                String m = rs.getString("Month").trim();
                String code = rs.getString("OrderCode");
                int orderGroupId = rs.getInt("OrderGroupId");

                if (m.length() == 1) {
                    m = "0" + m;
                }

                Order order = new  Order(country, y, m, code, orderGroupId);
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}