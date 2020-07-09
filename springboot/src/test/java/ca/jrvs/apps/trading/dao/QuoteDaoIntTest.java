package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
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
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;
  private final Quote savedQuote = new Quote();

  @Before
  public void insert() {
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);
  }

  @Test
  public void findTest() {
    List<Quote> quoteList = new ArrayList<>();
    quoteDao.findAll().forEach(quoteList::add);
    assertEquals(savedQuote.getId(), quoteList.get(0).getId());
  }

  @Test
  public void updateTest() {
    Quote updatedQuote = new Quote();
    updatedQuote.setAskPrice(20d);
    updatedQuote.setAskSize(20);
    updatedQuote.setBidPrice(30.2d);
    updatedQuote.setBidSize(30);
    updatedQuote.setId("AAPL");
    updatedQuote.setLastPrice(20.1d);
    quoteDao.save(updatedQuote);

    Optional<Quote> result = quoteDao.findById(savedQuote.getId());
    assertTrue(result.isPresent());
    assertEquals(updatedQuote.getId(), result.get().getId());
    assertEquals(updatedQuote.getAskPrice(), result.get().getAskPrice());
  }

  @Test
  public void existsTest() {
    Assert.assertTrue(quoteDao.existsById(savedQuote.getId()));
  }

  @Test
  public void countTest() {
    long count = quoteDao.count();
    assertEquals(1, count);
  }

  @Test
  public void failFindByIdTest() {
    Quote quote = quoteDao.findById("A123").orElse(null);
    assertNull(quote);
  }

  @After
  public void deleteOne() {
    quoteDao.deleteAll();
  }
}
