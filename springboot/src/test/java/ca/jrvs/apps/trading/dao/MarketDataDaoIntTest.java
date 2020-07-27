package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MarketDataDaoIntTest {

  private MarketDataDao marketDataDao;
  private QuoteDao quoteDao;

  @Before
  public void init() {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(50);
    cm.setDefaultMaxPerRoute(50);
    MarketDataConfig config = new MarketDataConfig();
    config.setHost("https://cloud.iexapis.com/v1/");
    config.setToken(System.getenv("IEX_PUB_TOKEN"));
    marketDataDao = new MarketDataDao(cm, config);
  }

  @Test
  public void findIexQuotesByTickers() throws IllegalArgumentException {
    //happy path
    List<IexQuote> quoteList = marketDataDao
        .findAllById(Arrays.asList("AAPL", "FB", "AMD", "MSFT"));
    assertEquals(4, quoteList.size());
    assertEquals("AAPL", quoteList.get(0).getSymbol());

    //sad path
    try {
      marketDataDao.findAllById(Arrays.asList("AAPL", "FB2"));
      Assert.fail();
    } catch (IllegalArgumentException ex) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void findIexQuoteByTicker() throws IllegalArgumentException {
    Optional<IexQuote> iexQuote1 = marketDataDao.findById("AAPL");
    Optional<IexQuote> iexQuote2 = marketDataDao.findById("MSFT");

    assertTrue(iexQuote1.isPresent());
    assertTrue(iexQuote2.isPresent());
  }
}
