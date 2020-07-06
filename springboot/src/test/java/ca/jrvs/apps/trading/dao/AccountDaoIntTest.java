package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
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
    Account account = accountDao.findById(1).orElse(null);
    assertNotNull(account);
    assertEquals(testAccount.getAmount(), account.getAmount());
  }

  @Test
  public void updateTest() {
    testAccount.setAmount(1000d);
    testAccount.setId(1);
    Account updatedAccount = accountDao.save(testAccount);

    assertEquals(testAccount.getAmount(), updatedAccount.getAmount());
  }

  @Test
  public void countTest() {
    long count = accountDao.count();
    assertEquals(1, count);
  }

  @Test
  public void findAllTest() {
    List<Account> accounts = new ArrayList<>();
    accountDao.findAll().forEach(accounts::add);

    assertEquals(1, accounts.size());
  }

  @Test
  public void saveAllTest() {
    Account account1 = new Account();
    Account account2 = new Account();
    account1.setTraderId(1);
    account1.setId(2);
    account1.setAmount(500d);
    account2.setTraderId(1);
    account2.setId(3);
    account2.setAmount(500d);
    List<Account> accounts = new ArrayList<Account>(Arrays.asList(account1, account2));
    List<Account> results = new ArrayList<>();
    accountDao.saveAll(accounts).forEach(results::add);

    assertEquals(2, results.size());

    accountDao.deleteById(2);
    accountDao.deleteById(3);
  }

  @After
  public void delete() {
    accountDao.deleteById(1);
    traderDao.deleteById(1);
  }
}
