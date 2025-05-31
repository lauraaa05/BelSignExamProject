package bll;

import be.Order;
import dal.OrderStatusDAO;

import java.util.List;

public class OrderStatusManager {
    private final OrderStatusDAO dao =  new OrderStatusDAO();

    public List<Order> getToDoOrders() {
        return dao.getOrdersByRoleAndStatuses("Operator", List.of("todo","rejected"));
    }

    public List<Order> getDoneOrders() {
        return dao.getOrdersByRoleAndStatuses("Quality Control", List.of("done"));
    }

    public String getStatusForOrder(String orderNumber) {
        return dao.getStatusForOrder(orderNumber);
    }

    public List<Order> getOrdersByRoleAndStatuses(String roleName, List<String> statuses) {
        return dao.getOrdersByRoleAndStatuses(roleName, statuses);
    }

    public boolean updateOrderStatusAndRole(String orderCode, int currentRoleId, int newRoleId, int newStatusId) {
        return dao.updateOrderStatusAndRole(orderCode, currentRoleId, newRoleId, newStatusId);
    }

    public boolean updateOrderStatus(String orderCode, int roleId, int newStatusId) {
        return dao.updateOrderStatus(orderCode, roleId, newStatusId);
    }
}