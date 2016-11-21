package data;

import models.Customer;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcCustomerDAO implements CustomerDAO {

    public DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Customer getCustomer(long id) {
        String sqlSearch = "SELECT * FROM customer WHERE id = ? LIMIT 1";
        Customer customer = (Customer) jdbcTemplate.queryForObject(sqlSearch, new Object[]{id}, new CustomerRowMapper());
        return customer;
    }

    @Override
    public boolean addCustomer(Customer customer) {

        String sqlInsert = "INSERT INTO customer (firstName,lastName)"
                + " VALUES (?, ?)";
        jdbcTemplate.update(sqlInsert, customer.getFirstName(), customer.getLastName());

        return true;
    }

    @Override
    public long getCustomerCount() {
        String sql = "SELECT COUNT(*) FROM customer";
        long total = jdbcTemplate.queryForObject(sql, Long.class);
        return total;
    }


    public class CustomerRowMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setFirstName(rs.getString("firstName"));
            customer.setLastName(rs.getString("lastName"));
            return customer;
        }

    }
}