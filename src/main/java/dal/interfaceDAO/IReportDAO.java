package dal.interfaceDAO;

import be.Report;
import exceptions.DALException;

import java.util.Map;

public interface IReportDAO {

    void insertReport(Report report) throws DALException;

    String getLatestCommentByOrderNumber(String orderCode) throws DALException;

    Map<String, String> getProductDetailsByOrderCode(String orderCode) throws DALException;

    void savePdfToDatabase(String orderNumber, byte[] pdfBytes) throws DALException;

    byte[] getPdfFromDatabase(String orderCode) throws DALException;

    void saveDoneReport(String orderCode, int signedBy) throws DALException;


}
