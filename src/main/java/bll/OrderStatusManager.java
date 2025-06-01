package bll;

import be.Order;
import dal.OrderStatusDAO;
import exceptions.BLLException;
import exceptions.DALException;

import java.util.List;

public class OrderStatusManager {
    private final OrderStatusDAO dao =  new OrderStatusDAO();

    public List<Order> getToDoOrders() throws BLLException {
        try {
            return dao.getOrdersByRoleAndStatuses("Operator", List.of("todo","rejected"));
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve orders.", e);
        }
    }

    public List<Order> getDoneOrders() throws BLLException {
        try {
            return dao.getOrdersByRoleAndStatuses("Quality Control", List.of("done"));
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve orders.", e);
        }
    }

    public String getStatusForOrder(String orderNumber) throws BLLException{
        try {
            return dao.getStatusForOrder(orderNumber);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve order status.", e);
        }
    }

    public List<Order> getOrdersByRoleAndStatuses(String roleName, List<String> statuses) throws BLLException {
        try {
            return dao.getOrdersByRoleAndStatuses(roleName, statuses);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve orders.", e);
        }
    }

    public boolean updateOrderStatusAndRole(String orderCode, int currentRoleId, int newRoleId, int newStatusId) throws BLLException{
        try {
            return dao.updateOrderStatusAndRole(orderCode, currentRoleId, newRoleId, newStatusId);
        } catch (DALException e) {
            throw new BLLException("Failed to update order status and role.", e);
        }
    }

    public boolean updateOrderStatus(String orderCode, int roleId, int newStatusId) throws BLLException {
        try {
            return dao.updateOrderStatus(orderCode, roleId, newStatusId);
        } catch (DALException e) {
            throw new BLLException("Failed to update order status.", e);
        }
    }
}