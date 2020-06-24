package ca.jrvs.apps.trading.dao;

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
  public void insertSamples() {
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
    Assert.assertEquals(savedQuote.getTicker(), quoteList.get(0).getTicker());
  }

  @Test
  public void updateTest() {
    Quote newSaved = new Quote();
    newSaved.setAskPrice(20d);
    newSaved.setAskSize(20);
    newSaved.setBidPrice(30.2d);
    newSaved.setBidSize(30);
    newSaved.setId("AAPL");
    newSaved.setLastPrice(20.1d);
    quoteDao.save(newSaved);

    Optional<Quote> result = quoteDao.findById(savedQuote.getId());
    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(newSaved.getTicker(), result.get().getTicker());
    Assert.assertEquals(newSaved.getAskPrice(), result.get().getAskPrice());
  }

  @Test
  public void existsTest() {
    Assert.assertTrue(quoteDao.existsById(savedQuote.getId()));
  }

  @After
  public void deleteOne() {
    quoteDao.deleteById(savedQuote.getId());
  }
}
