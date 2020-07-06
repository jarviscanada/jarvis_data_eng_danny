package ca.jrvs.apps.trading.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
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
public class PositionDaoIntTest {

  @Autowired
  private AccountDao accountDao;
  @Autowired
  private TraderDao traderDao;
  @Autowired
  private QuoteDao quoteDao;
  @Autowired
  private SecurityOrderDao securityOrderDao;
  @Autowired
  private PositionDao positionDao;

  private final Trader testTrader = new Trader();
  private final Account testAccount = new Account();
  private final Quote testQuote = new Quote();
  private final SecurityOrder testOrder = new SecurityOrder();

  @Before
  public void setup() {
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
  public void findByIdTest() {
    List<Position> positions = positionDao.findById(1);
    assertNotNull(positions);

    Position result = positions.get(0);
    assertEquals(testOrder.getTicker(), result.getTicker());
    assertEquals(testOrder.getSize(), result.getPosition());
  }

  @Test
  public void findByIdAndTickerTest() {
    String ticker = testOrder.getTicker();
    Integer id = testAccount.getId();
    Position position = positionDao.findByIdAndTicker(id, ticker).
        orElseThrow(() -> new IllegalArgumentException("Error querying Position view"));
  }

  @Test
  public void failFindById() {
    List<Position> positions = positionDao.findById(0);
    assertTrue(positions.isEmpty());
  }

  @Test
  public void failFindByIdAndTickerTest() {
    String ticker = testOrder.getTicker();
    Integer id = 0;
    Position position = positionDao.findByIdAndTicker(id, ticker).orElse(null);
    assertNull(position);
  }

  @Test
  public void findAllTest() {
    List<Position> positions = (List) positionDao.findAll();
    assertEquals(1, positions.size());
  }

  @Test
  public void countTest() {
    long count = positionDao.count();
    assertEquals(1, count);
  }

  @After
  public void delete() {
    securityOrderDao.deleteById(testOrder.getId());
    accountDao.deleteById(testAccount.getId());
    traderDao.deleteById(testTrader.getId());
    quoteDao.deleteById(testQuote.getId());
  }
}
