package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
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
    Assert.assertTrue(exists);
  }

  @Test
  public void countTest() {
    long count = securityOrderDao.count();
    Assert.assertEquals(1, count);
  }

  @Test
  public void updateTest() {
    testOrder.setPrice(40d);
    testOrder.setSize(30);
    SecurityOrder newOrder = securityOrderDao.save(testOrder);

    Assert.assertEquals(testOrder.getPrice(), newOrder.getPrice());
    Assert.assertEquals(testOrder.getSize(), newOrder.getSize());
  }

  @After
  public void delete() {
    securityOrderDao.deleteById(testOrder.getId());
    accountDao.deleteById(testAccount.getId());
    traderDao.deleteById(testTrader.getId());
    quoteDao.deleteById(testQuote.getId());
  }
}
