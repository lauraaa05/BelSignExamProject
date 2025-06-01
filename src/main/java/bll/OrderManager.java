package bll;
import be.Order;
import dal.OrderDAO;

import java.util.List;

public class OrderManager {

    private OrderDAO od = new OrderDAO();

    public List<Order> getOrdersForDate(int year, int month) {
        return od.getOrdersForDate(year, month);
    }

}