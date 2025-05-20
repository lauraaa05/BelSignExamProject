package bll;

import be.Order;
import dal.OrderStatusDAO;

import java.util.List;

public class OrderStatusManager {
    private final OrderStatusDAO dao =  new OrderStatusDAO();

    public List<Order> getToDoOrders() {
        return dao.getOrdersByRoleAndStatus("operator", "todo");
    }

    public List<Order> getDoneOrders() {
        return dao.getOrdersByRoleAndStatus("operator", "done");
    }
}