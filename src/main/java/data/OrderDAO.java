package data;

import models.Order;

public interface OrderDAO {

    Order getOrder(long id);

    boolean addOrder(Order order);



}
