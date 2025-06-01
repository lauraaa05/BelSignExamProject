package dal.interfaceDAO;

import exceptions.DALException;

import java.sql.SQLException;

public interface IDoneReportDAO {

    String getSignatureNameByOrderCode(String orderCode) throws SQLException;


}
