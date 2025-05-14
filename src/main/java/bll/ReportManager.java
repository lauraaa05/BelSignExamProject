package bll;

import be.Report;
import dal.ReportDAO;

import java.sql.SQLException;
import java.util.Map;

public class ReportManager {

    private final ReportDAO reportDAO = new ReportDAO();

    public void insertReport(Report report) throws SQLException {
        reportDAO.insertReport(report);
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws SQLException {
        return reportDAO.getLatestCommentByOrderNumber(orderNumber);
    }

    public Map<String, String> getProductDetailsByOrderCode(String orderCode) throws SQLException {
        return reportDAO.getProductDetailsByOrderCode(orderCode);
    }
}
