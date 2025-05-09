package bll;

import dal.OrderStatusDAO;

import java.util.List;

public class OrderStatusManager {
    private final OrderStatusDAO dao =  new OrderStatusDAO();

    public List<String> getToDoOrders() {
        return dao.getFormattedOrdersByRoleAndStatus("operator", "todo");
    }

    public List<String> getDoneOrders() {
        return dao.getFormattedOrdersByRoleAndStatus("operator", "done");
    }
}