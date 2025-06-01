package bll;

import be.Report;
import dal.DoneReportDAO;
import dal.ReportDAO;
import exceptions.BLLException;
import exceptions.DALException;

import java.sql.SQLException;
import java.util.Map;

public class ReportManager {

    private final ReportDAO reportDAO = new ReportDAO();
    private final DoneReportDAO doneReportDAO = new DoneReportDAO();

    public void insertReport(Report report) throws BLLException {
        try {
            reportDAO.insertReport(report);
        } catch (DALException e) {
            throw new BLLException("Failed to insert report.", e);
        }
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws BLLException {
        try {
            return reportDAO.getLatestCommentByOrderNumber(orderNumber);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve latest comment.", e);
        }
    }

    public Map<String, String> getProductDetailsByOrderCode(String orderCode) throws BLLException {
        try {
            return reportDAO.getProductDetailsByOrderCode(orderCode);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve product details.", e);
        }
    }

    public void saveDoneReport(String orderCode, int signedBy) throws BLLException {
        try {
            reportDAO.saveDoneReport(orderCode, signedBy);
        } catch (DALException e) {
            throw new BLLException("Failed to save done report.", e);
        }
    }

    public String getSignatureNameByOrderCode(String orderCode) throws BLLException {
        try {
            return doneReportDAO.getSignatureNameByOrderCode(orderCode);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve signature name.", e);
        }
    }
}