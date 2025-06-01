package dal.interfaceDAO;

import be.Order;

import java.util.List;

public interface IOrderStatusDAO {

    boolean updateOrderStatusAndRole(String orderCode, int currentRoleId, int newRoleId, int newStatusId);

    List<String> getRawOrderCodesByRoleAndStatus(String role, String status);

    String getStatusForOrder(String orderNumber);

    List<Order> getOrdersByRoleAndStatuses(String roleName, List<String> statuses);

    boolean updateOrderStatus(String orderCode, int roleId, int newStatusId);

}
