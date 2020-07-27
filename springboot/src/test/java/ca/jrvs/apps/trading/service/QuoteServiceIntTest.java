package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  @Before
  public void setup() {
    Quote quote1 = new Quote();
    quote1.setId("MSFT");
    quote1.setAskPrice(20d);
    quote1.setAskSize(20);
    quote1.setBidPrice(15d);
    quote1.setBidSize(15);
    quote1.setLastPrice(20d);
    quoteDao.save(quote1);
  }

  @Test
  public void findIexQuoteByTickerTest() {
    try {
      quoteService.findIexQuoteByTicker("AAAPL");
      fail();
    } catch (DataRetrievalFailureException ex) {
      assertTrue(true);
    }

    IexQuote iexQuote = quoteService.findIexQuoteByTicker("AAPL");
    assertNotNull(iexQuote);
    assertEquals("AAPL", iexQuote.getSymbol());
  }

  @Test
  public void saveQuotesTest() {
    List<String> tickers = Arrays.asList(new String[] {"MSFT", "AMD", "INTC"});
    List<Quote> quotes = quoteService.saveQuotes(tickers);
    assertNotNull(quotes);
    assertEquals(3, quotes.size());
  }

  @Test
  public void saveQuoteTest() {
    String ticker = "TSLA";
    Quote quote = quoteService.saveQuote(ticker);
    assertNotNull(quote);
  }

  @After
  public void teardown() {
    quoteDao.deleteAll();
  }
}
