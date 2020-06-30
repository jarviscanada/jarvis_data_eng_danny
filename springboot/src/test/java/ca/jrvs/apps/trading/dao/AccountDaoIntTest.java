package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
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
public class AccountDaoIntTest {

  @Autowired
  private AccountDao accountDao;
  @Autowired
  private TraderDao traderDao;

  private final Account testAccount = new Account();
  private final Trader testTrader = new Trader();

  @Before
  public void insert() {
    testTrader.setFirstName("Only");
    testTrader.setLastName("Puts");
    testTrader.setCountry("Canada");
    testTrader.setEmail("gmail");
    testTrader.setDob(new Date(1990, 10, 10));
    traderDao.save(testTrader);

    testAccount.setTraderId(1);
    testAccount.setAmount(500d);
    accountDao.save(testAccount);
  }

  @Test
  public void findTest() {
    Optional<Account> account = accountDao.findById(1);
    Assert.assertNotNull(account.get());
    Assert.assertEquals(testAccount.getAmount(), account.get().getAmount());
  }

  @Test
  public void updateTest() {
    testAccount.setAmount(1000d);
    testAccount.setId(1);
    Account updatedAccount = accountDao.save(testAccount);

    Assert.assertEquals(testAccount.getAmount(), updatedAccount.getAmount());
  }

  @After
  public void delete() {
    accountDao.deleteById(1);
    traderDao.deleteById(1);
  }
}
