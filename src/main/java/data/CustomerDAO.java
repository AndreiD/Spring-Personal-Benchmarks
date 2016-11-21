package data;

import models.Customer;

public interface CustomerDAO {

    Customer getCustomer(long id);

    boolean addCustomer(Customer customer);

    long getCustomerCount();


}
