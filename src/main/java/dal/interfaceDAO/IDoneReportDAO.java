package dal.interfaceDAO;

import java.sql.SQLException;

public interface IDoneReportDAO {

    String getSignatureNameByOrderCode(String orderCode) throws SQLException;
}