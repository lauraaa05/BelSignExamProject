package bll;
import be.Order;
import dal.OrderDAO;
import exceptions.BLLException;
import exceptions.DALException;

import java.util.List;

public class OrderManager {

    private OrderDAO od = new OrderDAO();

    public List<Order> getOrdersForDate(int year, int month) throws BLLException {
        try {
            return od.getOrdersForDate(year, month);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve orders.", e);
        }
    }

}