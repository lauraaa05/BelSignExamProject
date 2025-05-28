package bll;

import be.Report;
import dal.DoneReportDAO;
import dal.ReportDAO;

import java.sql.SQLException;
import java.util.Map;

public class ReportManager {

    private final ReportDAO reportDAO = new ReportDAO();
    private final DoneReportDAO doneReportDAO = new DoneReportDAO();

    public void insertReport(Report report) throws SQLException {
        reportDAO.insertReport(report);
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws SQLException {
        return reportDAO.getLatestCommentByOrderNumber(orderNumber);
    }

    public Map<String, String> getProductDetailsByOrderCode(String orderCode) throws SQLException {
        return reportDAO.getProductDetailsByOrderCode(orderCode);
    }

    public void saveDoneReport(String orderCode, int signedBy) throws SQLException {
        reportDAO.saveDoneReport(orderCode, signedBy);
    }

    public String getSignatureNameByOrderCode(String orderCode) throws SQLException {
        return doneReportDAO.getSignatureNameByOrderCode(orderCode);
    }
}
