package dal;

import be.Report;
import dal.interfaceDAO.IReportDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportDAO implements IReportDAO {

    public void insertReport(Report report) throws DALException {
        String sql = "INSERT INTO Reports (UserId, Comment, OrderNumber, Date, OrderCode) VALUES (?,?,?,?,?)";
        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, report.getUserId());
            stmt.setString(2,report.getComment());
            stmt.setString(3,report.getOrderNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(report.getDate()));
            stmt.setString(5, report.getOrderCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException("Failed to insert report.", e);
        }
    }

    public String getLatestCommentByOrderNumber(String orderCode) throws DALException {
        String sql = "SELECT TOP 1 Comment FROM Reports WHERE OrderCode = ? ORDER BY Date DESC";
        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Comment");
            }
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve latest comment.", e);
        }
        return "No comments yet.";
    }

    public Map<String, String> getProductDetailsByOrderCode(String orderCode) throws DALException {
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
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve product details.", e);
        }
        return null;
    }

    public void savePdfToDatabase(String orderNumber, byte[] pdfBytes) throws DALException {
        String sql = "UPDATE DoneReport SET PdfData = ? WHERE OrderCode = ?";

        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBytes(1, pdfBytes);
            stmt.setString(2, orderNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException("Failed to save PDF to database.", e);
        }
    }

    public byte[] getPdfFromDatabase(String orderCode) throws DALException {
        String sql = "SELECT PdfData FROM DoneReport WHERE OrderCode = ?";

        try (Connection conn = DBAccess.DBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("PdfData");
            }
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve PDF from database.", e);
        }
        return null;
    }

    public void saveDoneReport(String orderCode, int signedBy) throws DALException {
        String sql = "INSERT INTO DoneReport (OrderCode, Date, SignedBy) VALUES (?, GETDATE(), ?)";

        try (Connection conn = DBAccess.DBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderCode);
            stmt.setInt(2, signedBy);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException("Failed to save done report.", e);
        }
    }
}
