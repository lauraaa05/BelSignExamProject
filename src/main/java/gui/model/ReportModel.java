package gui.model;

import be.Report;
import bll.ReportManager;
import dal.ReportDAO;
import exceptions.BLLException;
import exceptions.DALException;

import java.sql.SQLException;
import java.util.Map;

public class ReportModel {

    private final ReportManager reportManager = new ReportManager();
    private final ReportDAO reportDAO = new ReportDAO();

    public void insertReport(Report report) throws BLLException {
        reportManager.insertReport(report);
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws BLLException {
        return reportManager.getLatestCommentByOrderNumber(orderNumber);
    }

    public Map<String, String> getProductDetailsByOrderCode(String orderNumber) throws BLLException {
        return reportManager.getProductDetailsByOrderCode(orderNumber);
    }

    public void savePdfToDatabase(String orderNumber, byte[] pdfBytes) throws DALException {
        reportDAO.savePdfToDatabase(orderNumber, pdfBytes);
    }

    public byte[] getPdfFromDatabase(String orderCode) throws DALException {
        return  reportDAO.getPdfFromDatabase(orderCode);
    }

    public void saveDoneReport(String orderCode, int signedBy) throws BLLException {
        reportManager.saveDoneReport(orderCode, signedBy);
    }

    public String getSignatureNameByOrderCode(String orderCode) throws SQLException {
        return reportManager.getSignatureNameByOrderCode(orderCode);
    }
}