package dal;

import be.OrderStatusLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusLogDAO {

    private final DBAccess dbAccess = new DBAccess();

    public void insertLog(OrderStatusLog orderStatusLog) throws SQLException {
        String sql = "INSERT INTO OrderStatusOrder (OrderCode, OrderStatus, UserRole, LastUpdated) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderStatusLog.getOrderCode());
            stmt.setInt(2, orderStatusLog.getStatusId());
            stmt.setInt(3, orderStatusLog.getUserRoleId());
            stmt.setTimestamp(4, Timestamp.valueOf(orderStatusLog.getDateChanged()));

            stmt.executeUpdate();
        }
    }

    public List<OrderStatusLog> getLogsForOrder(String orderCode) throws SQLException {
        List<OrderStatusLog> orderStatusLogs = new ArrayList<>();
        String sql = "SELECT * FROM OrderStatusOrder WHERE OrderCode = ?";

        try (Connection conn = dbAccess.DBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderStatusLog log = new OrderStatusLog(
                        rs.getString("OrderCode"),
                        rs.getInt("OrderStatus"),
                        rs.getInt("UserRole"),
                        rs.getTimestamp("LastUpdated").toLocalDateTime()
                );
             orderStatusLogs.add(log);
            }
        }
        return orderStatusLogs;
    }
}