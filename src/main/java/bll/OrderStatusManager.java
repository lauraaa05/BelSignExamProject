package bll;

import be.Order;
import dal.OrderStatusDAO;

import java.util.List;

public class OrderStatusManager {
    private final OrderStatusDAO dao =  new OrderStatusDAO();

    public List<Order> getToDoOrders() {
        return dao.getOrdersByRoleAndStatuses("operator", List.of("todo","rejected"));
    }

    public List<Order> getDoneOrders() {
        return dao.getOrdersByRoleAndStatuses("qcu", List.of("done"));
    }
}