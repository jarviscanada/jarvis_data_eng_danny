package ca.jrvs.apps.trading.dao;

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
    Assert.assertEquals(testTrader.getFirstName(), traderList.get(0).getFirstName());
    Assert.assertEquals(testTrader.getLastName(), traderList.get(0).getLastName());
    Assert.assertEquals(testTrader.getDob(), traderList.get(0).getDob());
  }

  @Test
  public void findByIdTest() {
    Trader trader = traderDao.findById(1).get();
    Assert.assertNotNull(trader);
    Assert.assertEquals(testTrader.getFirstName(), trader.getFirstName());
    Assert.assertEquals(testTrader.getLastName(), trader.getLastName());
    Assert.assertEquals(testTrader.getDob(), trader.getDob());
  }

  @Test
  public void findNonexistent() {
    Optional<Trader> trader = traderDao.findById(0);
    Assert.assertEquals(Optional.empty(), trader);
  }

  @Test
  public void countTest() {
    long count = traderDao.count();
    Assert.assertEquals(1, count);
  }

  @Test
  public void updateTest() {
    testTrader.setCountry("USA");
    testTrader.setEmail("hotmail");
    Trader newTrader = traderDao.save(testTrader);
    Assert.assertNotNull(newTrader);
    Assert.assertEquals("USA", newTrader.getCountry());
    Assert.assertEquals("hotmail", newTrader.getEmail());
  }

  @After
  public void delete() {
    traderDao.deleteById(testTrader.getId());
  }
}
