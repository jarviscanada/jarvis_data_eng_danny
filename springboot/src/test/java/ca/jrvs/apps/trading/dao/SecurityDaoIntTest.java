package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class SecurityDaoIntTest {

  @Autowired
  private AccountDao accountDao;
  @Autowired
  private TraderDao traderDao;
  @Autowired
  private QuoteDao quoteDao;
  @Autowired
  private SecurityOrderDao securityOrderDao;

  private final Trader testTrader = new Trader();
  private final Account testAccount = new Account();
  private final Quote testQuote = new Quote();
  private final SecurityOrder testOrder = new SecurityOrder();

  @Before
  public void insert() {
    testTrader.setFirstName("Only");
    testTrader.setLastName("Puts");
    testTrader.setCountry("Canada");
    testTrader.setEmail("gmail");
    testTrader.setDob(new Date(1990, 10, 10));
    testTrader.setId(1);
    traderDao.save(testTrader);
    testAccount.setTraderId(1);
    testAccount.setAmount(500d);
    testAccount.setId(1);
    accountDao.save(testAccount);
    testQuote.setAskPrice(10d);
    testQuote.setAskSize(10);
    testQuote.setBidPrice(10.2d);
    testQuote.setBidSize(10);
    testQuote.setId("AAPL");
    testQuote.setLastPrice(10.1d);
    quoteDao.save(testQuote);

    testOrder.setAccountId(1);
    testOrder.setNotes("test notes");
    testOrder.setPrice(30d);
    testOrder.setSize(20);
    testOrder.setStatus("FILLED");
    testOrder.setTicker("AAPL");
    testOrder.setId(1);
    securityOrderDao.save(testOrder);
  }

  @Test
  public void existsTest() {
    System.out.println(testOrder.getId());
    Boolean exists = securityOrderDao.existsById(testOrder.getId());
    assertTrue(exists);
  }

  @Test
  public void countTest() {
    long count = securityOrderDao.count();
    assertEquals(1, count);
  }

  @Test
  public void updateTest() {
    testOrder.setPrice(40d);
    testOrder.setSize(30);
    SecurityOrder newOrder = securityOrderDao.save(testOrder);

    assertEquals(testOrder.getPrice(), newOrder.getPrice());
    assertEquals(testOrder.getSize(), newOrder.getSize());
  }

  @Test
  public void findByIdTest() {
    SecurityOrder order = securityOrderDao.findById(testOrder.getId()).orElse(null);
    assertNotNull(order);
    assertEquals(order.getId(), testOrder.getId());
    assertEquals(order.getAccountId(), testOrder.getAccountId());
    assertEquals(order.getTicker(), testOrder.getTicker());
  }

  @Test
  public void failFindByIdTest() {
    SecurityOrder order = securityOrderDao.findById(0).orElse(null);
    assertNull(order);
  }

  @Test
  public void saveAllTest() {
    SecurityOrder order1 = new SecurityOrder();
    SecurityOrder order2 = new SecurityOrder();
    order1.setAccountId(1);
    order1.setSize(20);
    order1.setStatus("FILLED");
    order1.setTicker("AAPL");
    order1.setId(2);
    order2.setAccountId(1);
    order2.setSize(20);
    order2.setStatus("FILLED");
    order2.setTicker("AAPL");
    order2.setId(3);

    List<SecurityOrder> orders = new ArrayList<>(Arrays.asList(order1, order2));
    List<SecurityOrder> results = new ArrayList<>();
    securityOrderDao.saveAll(orders).forEach(results::add);

    assertEquals(2, results.size());
    assertEquals(order1.getAccountId(), results.get(0).getAccountId());
    assertEquals(order2.getAccountId(), results.get(1).getAccountId());

    securityOrderDao.deleteById(order1.getId());
    securityOrderDao.deleteById(order2.getId());
  }

  @Test
  public void findAllTest() {
    List<SecurityOrder> orders = new ArrayList<>();
    securityOrderDao.findAll().forEach(orders::add);

    assertEquals(1, orders.size());
  }

  @After
  public void delete() {
    securityOrderDao.deleteByAccountId(testAccount.getId());
    accountDao.deleteById(testAccount.getId());
    traderDao.deleteById(testTrader.getId());
    quoteDao.deleteById(testQuote.getId());
  }
}
