package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class TraderDaoIntTest {

  @Autowired
  private TraderDao traderDao;
  private final Trader testTrader = new Trader();

  @Before
  public void insert() {
    testTrader.setId(1);
    testTrader.setFirstName("Only");
    testTrader.setLastName("Puts");
    testTrader.setCountry("Canada");
    testTrader.setEmail("gmail");
    testTrader.setDob(new Date(1990, 10, 10));
    traderDao.save(testTrader);
  }

  @Test
  public void findAllTest() {
    List<Trader> traderList = new ArrayList<>();
    traderDao.findAll().forEach(traderList::add);
    assertEquals(testTrader.getFirstName(), traderList.get(0).getFirstName());
    assertEquals(testTrader.getLastName(), traderList.get(0).getLastName());
    assertEquals(testTrader.getDob(), traderList.get(0).getDob());
  }

  @Test
  public void findByIdTest() {
    Trader trader = traderDao.findById(1).get();
    assertNotNull(trader);
    assertEquals(testTrader.getFirstName(), trader.getFirstName());
    assertEquals(testTrader.getLastName(), trader.getLastName());
    assertEquals(testTrader.getDob(), trader.getDob());
  }

  @Test
  public void findNonExistent() {
    Optional<Trader> trader = traderDao.findById(0);
    assertEquals(Optional.empty(), trader);
  }

  @Test
  public void countTest() {
    long count = traderDao.count();
    assertEquals(1, count);
  }

  @Test
  public void updateTest() {
    testTrader.setCountry("USA");
    testTrader.setEmail("hotmail");
    traderDao.save(testTrader);
    Trader newTrader = traderDao.findById(testTrader.getId()).orElse(null);
    assertNotNull(newTrader);
    assertEquals("USA", newTrader.getCountry());
    assertEquals("hotmail", newTrader.getEmail());
  }

  @After
  public void delete() {
    traderDao.deleteById(testTrader.getId());
  }
}
