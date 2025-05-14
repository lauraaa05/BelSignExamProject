package dal;

import be.Report;

import java.sql.*;

public class ReportDAO {

    public void insertReport(Report report) throws SQLException {
        String sql = "INSERT INTO Reports (UserId, Comment, OrderNumber, Date, OrderCode) VALUES (?,?,?,?,?)";
        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, report.getUserId());
            stmt.setString(2,report.getComment());
            stmt.setString(3,report.getOrderNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(report.getDate()));
            stmt.setString(5, report.getOrderCode());
            stmt.executeUpdate();
        }
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws SQLException {
        String sql = "SELECT TOP 1 Comment FROM Reports WHERE OrderNumber = ? ORDER BY Date DESC";
        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Comment");
            }
        }
        return "No comments yet.";
    }
}
