package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = "stock/market/batch?symbols=%s&types=quote&token=";
  private final String IEX_BATCH_URL;
  private static final int HTTP_OK = 200;

  private final String tickerRegex = "[a-zA-Z]{1,5}";
  private final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private final HttpClientConnectionManager httpClientConnectionManager;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig) {
    BasicConfigurator.configure();
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  /**
   * Get an IexQuote by calling findAllById
   *
   * @param ticker
   * @return
   * @throws IllegalArgumentException      if a given ticker is invalid
   * @throws DataRetrievalFailureException if HTTP request failed
   */
  @Override
  public Optional<IexQuote> findById(String ticker) {
    validateTicker(ticker);
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if (quotes.size() == 0) {
      return Optional.empty();
    } else if (quotes.size() == 1) {
      iexQuote = Optional.of(quotes.get(0));
    } else {
      throw new DataRetrievalFailureException("Unexpected number of quotes");
    }
    return iexQuote;
  }

  /**
   * Get quotes from IEX
   *
   * @param tickers is a list of tickers
   * @return a list of IexQuote object
   * @throws IllegalArgumentException      if any ticker in invalid or tickers is empty
   * @throws DataRetrievalFailureException if HTTP request is failed
   */
  @Override
  public List<IexQuote> findAllById(Iterable<String> tickers) {
    for (String ticker : tickers) {
      validateTicker(ticker);
    }

    List<IexQuote> quotes = new ArrayList<IexQuote>();
    String target = "%s";
    String symbols = String.join(",", tickers);
    String url = IEX_BATCH_URL.replace(target, symbols);
    logger.debug(url);

    String responseBody = executeHttpGet(url).get();
    logger.debug(responseBody);
    JSONObject bodyJson = new JSONObject(responseBody);
    for (String ticker : tickers) {
      try {
        JSONObject quoteJson = (JSONObject) bodyJson.get(ticker);
        logger.debug(quoteJson.toString());
        quotes.add(DaoUtils.toObjectFromJson(quoteJson.get("quote").toString(), IexQuote.class));
      } catch (IOException ex) {
        logger.error("Error parsing JSON to object", ex);
        throw new RuntimeException(ex);
      }
    }

    return quotes;
  }

  /**
   * Executes a get and returns a http entity/body as a string
   *
   * @param url resource URL
   * @return http response body or Optional.empty for 404
   * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
   */
  private Optional<String> executeHttpGet(String url) {
    CloseableHttpClient httpClient = getHttpClient();
    HttpGet httpGet = new HttpGet(url);
    HttpResponse response;
    String jsonString = "";
    logger.debug(url);
    try {
      response = httpClient.execute(httpGet);
      int responseStatus = response.getStatusLine().getStatusCode();
      if (responseStatus == HTTP_OK) {
        jsonString = EntityUtils.toString(response.getEntity());
      } else {
        throw new DataRetrievalFailureException("Unexpected HTTP status: " + responseStatus);
      }
    } catch (IOException ex) {
      logger.error(ex.getMessage(), ex);
      throw new RuntimeException();
    }
    return Optional.of(jsonString);
  }

  /**
   * Borrow an HTTP client from the HttpClientConnectionManager
   *
   * @return
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }

  /**
   * Validate tickers
   *
   * @throws IllegalArgumentException
   */
  private void validateTicker(String ticker) throws IllegalArgumentException {
    if (!ticker.matches(tickerRegex)) {
      throw new IllegalArgumentException("Illegal ticker.");
    }
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(IexQuote ieXQuote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> S save(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
