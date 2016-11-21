package base;


import com.fasterxml.jackson.databind.ObjectMapper;
import data.JdbcCustomerDAO;
import models.Customer;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class CustomerController {

    @Autowired
    DataSource dataSource;

    @RequestMapping(value = "/customer", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Customer> greeting(@RequestBody String payload) {
        System.out.println("Payload: " + payload);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Customer customer = objectMapper.readValue(payload, Customer.class);


            JdbcCustomerDAO jdbcCustomerDAO = new JdbcCustomerDAO();
            jdbcCustomerDAO.setDataSource(dataSource);

            jdbcCustomerDAO.addCustomer(customer);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/customer/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCustomerCount() {

        JdbcCustomerDAO jdbcCustomerDAO = new JdbcCustomerDAO();
        jdbcCustomerDAO.setDataSource(dataSource);

        long customer_cont = jdbcCustomerDAO.getCustomerCount();

        return new ResponseEntity<>(customer_cont, HttpStatus.OK);

    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET, params = {"id"})
    public ResponseEntity<Customer> getCustomer(@RequestParam long id) {


        JdbcCustomerDAO jdbcCustomerDAO = new JdbcCustomerDAO();
        jdbcCustomerDAO.setDataSource(dataSource);
        Customer customer = jdbcCustomerDAO.getCustomer(id);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}