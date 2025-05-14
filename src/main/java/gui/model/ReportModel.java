package gui.model;

import be.Report;
import bll.ReportManager;

import java.sql.SQLException;

public class ReportModel {

    private final ReportManager reportManager = new ReportManager();

    public void insertReport(Report report) throws SQLException {
        reportManager.insertReport(report);
    }

    public String getLatestCommentByOrderNumber(String orderNumber) throws SQLException {
        return reportManager.getLatestCommentByOrderNumber(orderNumber);
    }
}
