package bll;
import dal.OrderDAO;

import java.io.File;
import java.util.List;

public class OrderManager {

    private OrderDAO od = new OrderDAO();

    public List<String> getOrderNumbersAsList() {
        return od.getFormattedOrderNumbers();
    }

    public List<File> getOrderImages(String orderNumber) {
        return List.of();
    }

    public List<String> getOrdersForDate(int year, int month) {
        return od.getOrdersForDate(year, month);
    }
}