package base;


import com.fasterxml.jackson.databind.ObjectMapper;
import data.JdbcOrderDAO;
import models.Order;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class OrderController {

    @Autowired
    DataSource dataSource;

    @RequestMapping(value = "/order", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Order> add_new_order(@RequestBody String payload) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Order order = objectMapper.readValue(payload, Order.class);


            JdbcOrderDAO jdbcOrderDAO = new JdbcOrderDAO();
            jdbcOrderDAO.setDataSource(dataSource);

            if (jdbcOrderDAO.addOrder(order)) {
                return new ResponseEntity<>(order, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/order/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getCustomerCount() {

        JdbcOrderDAO jdbcOrderDAO = new JdbcOrderDAO();
        jdbcOrderDAO.setDataSource(dataSource);

        long customer_cont = jdbcOrderDAO.getOrdersCount();

        return new ResponseEntity<>(customer_cont, HttpStatus.OK);

    }

    @RequestMapping(value = "/order", method = RequestMethod.GET, params = {"id"})
    public ResponseEntity<Order> getCustomer(@RequestParam long id) {


        JdbcOrderDAO jdbcOrderDAO = new JdbcOrderDAO();
        jdbcOrderDAO.setDataSource(dataSource);
        Order customer = jdbcOrderDAO.getOrder(id);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}