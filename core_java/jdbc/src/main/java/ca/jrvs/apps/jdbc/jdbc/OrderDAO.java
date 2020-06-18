package ca.jrvs.apps.jdbc.jdbc;

import ca.jrvs.apps.jdbc.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderDAO extends DataAccessObject<Order> {
  final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);
  private final String GET_BY_ID = "SELECT "
      + "c.first_name, c.last_name, c.email, o.order_id, o.creation_date, o.total_due, o.status, "
      + "s.first_name, s.last_name, s.email, ol.quantity, p.code, p.name, p.size, p.variety, p.price "
      + "FROM orders o "
      + "JOIN customer c ON o.customer_id=c.customer_id "
      + "JOIN salesperson s ON o.salesperson_id=s.salesperson_id "
      + "JOIN order_item ol ON ol.order_id=o.order_id "
      + "JOIN product p ON ol.product_id=p.product_id "
      + "WHERE o.order_id=?";

  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    try (PreparedStatement statement = this.connection.prepareStatement(GET_BY_ID);) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      List<OrderItem> orderItems = new ArrayList<OrderItem>();

      long orderId = 0;
      while (rs.next()) {
        if (orderId == 0) {
          orderId = order.getId();
          order.setCustomerFirstName(rs.getString(1));
          order.setCustomerLastName(rs.getString(2));
          order.setCustomerEmail(rs.getString(3));
          order.setId(rs.getLong(4));
          order.setCreationDate(rs.getDate(5));
          order.setTotalDue(rs.getBigDecimal(6));
          order.setStatus(rs.getString(7));
          order.setSalespersonFirstName(rs.getString(8));
          order.setSalespersonLastName(rs.getString(9));
          order.setCustomerEmail(rs.getString(10));
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(rs.getInt(11));
        orderItem.setProductCode(rs.getString(12));
        orderItem.setProductName(rs.getString(13));
        orderItem.setProductSize(rs.getInt(14));
        orderItem.setProductVariety(rs.getString(15));
        orderItem.setProductPrice(rs.getBigDecimal(16));
        orderItems.add(orderItem);
      }
      order.setOrderItemList(orderItems);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return order;
  }

  @Override
  public List<Order> findAll() {
    return null;
  }

  @Override
  public Order update(Order dto) {
    return null;
  }

  @Override
  public Order create(Order dto) {
    return null;
  }

  @Override
  public void delete(long id) {

  }
}
