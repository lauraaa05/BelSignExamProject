package dal.interfaceDAO;

import be.Order;

import java.util.List;

public interface IOrderDAO extends IBaseDAO<Order> {
    static List<Order> getFormattedOrderNumbers() {
        return null;
    }




}
