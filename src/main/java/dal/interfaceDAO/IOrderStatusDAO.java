package dal.interfaceDAO;

import be.Order;
import exceptions.DALException;

import java.util.List;

public interface IOrderStatusDAO {

    boolean updateOrderStatusAndRole(String orderCode, int currentRoleId, int newRoleId, int newStatusId) throws DALException;

    String getStatusForOrder(String orderNumber) throws DALException;

    List<Order> getOrdersByRoleAndStatuses(String roleName, List<String> statuses) throws DALException;

    boolean updateOrderStatus(String orderCode, int roleId, int newStatusId) throws DALException;

}