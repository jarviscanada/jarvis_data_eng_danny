package ca.jrvs.apps.jdbc.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCExecutor {
  final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

  public static void main(String[] args) {
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "hplussport", "postgres", "pass");
    BasicConfigurator.configure();

    try {
      Connection connection = dcm.getConnection();
      OrderDAO orderDAO = new OrderDAO(connection);
      Order order = orderDAO.findById(1000);
      System.out.println(order);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}