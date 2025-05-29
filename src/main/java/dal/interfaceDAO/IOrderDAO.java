package dal.interfaceDAO;

import be.Order;

public interface IOrderDAO extends IBaseDAO<Order> {
    Order findOrderByOrderNumber(String orderNumber);
}
