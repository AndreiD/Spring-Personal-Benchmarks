package data;

import models.Order;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcOrderDAO implements OrderDAO {

    public DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public long getOrdersCount() {
        String sql = "SELECT COUNT(*) FROM benchmark_table";
        long total = jdbcTemplate.queryForObject(sql, Long.class);
        return total;
    }

    @Override
    public Order getOrder(long id) {
        String sqlSearch = "SELECT * FROM benchmark_table WHERE id = ? LIMIT 1";
        try {
            Order order = (Order) jdbcTemplate.queryForObject(sqlSearch, new Object[]{id}, new CustomerRowMapper());
            return order;
        } catch (DataAccessException dae) {
            return null;
        }
    }

    @Override
    public boolean addOrder(Order order) {

        //check if the record is already present
        String sql = "SELECT COUNT(*) FROM benchmark_table WHERE transaction_id = ?";
        long already_present = jdbcTemplate.queryForObject(sql, new Object[]{order.getTransaction_id()}, Long.class);
        if (already_present > 0) {
            return false;
        }

        String sqlInsert = "INSERT INTO benchmark_table (created_on, payment_method, transaction_id,product_id,product_name,product_quantity, product_price,product_currency)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, order.getCreated_on(), order.getPayment_method(), order.getTransaction_id(), order.getProduct_id(), order.getProduct_name(), order.getProduct_quantity(), order.getProduct_price(), order.getProduct_currency());

        return true;
    }


    public class CustomerRowMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setCreated_on(rs.getLong("created_on"));
            order.setPayment_method(rs.getString("payment_method"));
            order.setTransaction_id(rs.getString("transaction_id"));
            order.setProduct_id(rs.getString("product_id"));
            order.setProduct_name(rs.getString("product_name"));
            order.setProduct_quantity(rs.getInt("product_quantity"));
            order.setProduct_price(rs.getLong("product_price"));
            order.setProduct_currency(rs.getString("product_currency"));
            return order;
        }

    }
}