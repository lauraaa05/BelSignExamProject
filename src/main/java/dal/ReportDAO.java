package dal;

import be.Report;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> getProductDetailsByOrderCode(String orderCode) throws SQLException {
        String sql = "SELECT MaterialType, Color, Weight, Height, Length, Width FROM ProductDetails WHERE OrderCode = ?";
        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, String> details = new HashMap<>();
                details.put("MaterialType", rs.getString("MaterialType"));
                details.put("Color", rs.getString("Color"));
                details.put("Weight",rs.getString("Weight") + " meter");
                details.put("Height", rs.getString("Height") + " meter");
                details.put("Length", rs.getString("Length") + " meter");
                details.put("Width", rs.getString("Width") + " meter");
                return details;

            }
        }
        return null;
    }

    public void savePdfToDatabase(String orderNumber, byte[] pdfBytes) throws SQLException {
        String sql = "UPDATE DoneReports SET PdfData = ? WHERE OrderCode = ?";

        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBytes(1, pdfBytes);
            stmt.setString(2, orderNumber);
            stmt.executeUpdate();
        }
    }

    public byte[] getPdfFromDatabase(String orderCode) throws SQLException {
        String sql = "SELECT PdfData FROM DoneReports WHERE OrderCode = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("PdfData");
            }
        }

        return null;
    }
}
