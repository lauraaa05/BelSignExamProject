package dal;

import be.Order;
import dal.interfaceDAO.IOrderDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderDAO implements IOrderDAO {

    public OrderDAO() {
    }

    public List<Order> getOrdersForDate(int year, int month) throws DALException {
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
            throw new DALException("Failed to retrieve orders for date: " + year + "-" + month, e);
        }

        return orders;
    }

    @Override
    public void save(Order obj) {

    }

    @Override
    public void update(Order obj) {

    }

    @Override
    public void delete(Order obj) {

    }

    @Override
    public Order findById(int id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }
}