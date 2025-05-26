package gui.model;

import be.Report;
import bll.ReportManager;
import dal.ReportDAO;

import java.sql.SQLException;
import java.util.Map;

public class ReportModel {

    private final ReportManager reportManager = new ReportManager();
    private final ReportDAO reportDAO = new ReportDAO();

    public void insertReport(Report report) throws SQLException {
        reportManager.insertReport(report);
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws SQLException {
        return reportManager.getLatestCommentByOrderNumber(orderNumber);
    }

    public Map<String, String> getProductDetailsByOrderCode(String orderNumber) throws SQLException {
        return reportManager.getProductDetailsByOrderCode(orderNumber);
    }

    public void savePdfToDatabase(String orderNumber, byte[] pdfBytes) throws SQLException {
        reportDAO.savePdfToDatabase(orderNumber, pdfBytes);
    }

    public byte[] getPdfFromDatabase(String orderCode) throws SQLException {
        return  reportDAO.getPdfFromDatabase(orderCode);
    }

}
