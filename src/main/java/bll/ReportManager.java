package bll;

import be.Report;
import dal.ReportDAO;

import java.sql.SQLException;

public class ReportManager {

    private final ReportDAO reportDAO = new ReportDAO();

    public void insertReport(Report report) throws SQLException {
        reportDAO.insertReport(report);
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws SQLException {
        return reportDAO.getLatestCommentByOrderNumber(orderNumber);
    }
}
