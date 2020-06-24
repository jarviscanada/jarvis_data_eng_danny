package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class QuoteService {

  private final QuoteDao quoteDao;
  private final MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  public List<Quote> updateMarketData() {
    List<String> tickers = new ArrayList<>();
    quoteDao.findAll().forEach(quote -> tickers.add(quote.getTicker()));

    return saveQuotes(tickers);
//    List<IexQuote> iexQuotes = marketDataDao.findAllById(tickers);
//    List<Quote> quotes = iexQuotes.stream().map(
//        iexQuote -> buildQuoteFromIexQuote(iexQuote)).collect(Collectors.toList());
//
//    List<Quote> savedQuotes = new ArrayList<>();
//    quoteDao.saveAll(quotes).forEach(savedQuotes::add);
//    return savedQuotes;
  }

  protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
    return new Quote(iexQuote.getSymbol(), iexQuote.getLatestPrice(),
        iexQuote.getIexBidPrice(), iexQuote.getIexBidSize(), iexQuote.getIexAskPrice(),
        iexQuote.getIexAskSize());
  }

  /**
   * Find an IexQuote
   *
   * @param ticker id
   * @return IexQuote object
   * @throws IllegalArgumentException if ticker is invalid
   */
  public IexQuote findIexQuoteById(String ticker) {
    return marketDataDao.findById(ticker)
        .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid"));
  }

  /**
   * Validate (against IEX) and save given tickers to quote table
   * <p>
   * - Get iexQuote(s) - convert each iexQuote to Quote entity - persist the quote to db
   *
   * @param tickers
   * @return
   */
  public List<Quote> saveQuotes(List<String> tickers) {
    List<IexQuote> iexQuotes = marketDataDao.findAllById(tickers);
    List<Quote> quotes = iexQuotes.stream().map(
        iexQuote -> buildQuoteFromIexQuote(iexQuote)).collect(Collectors.toList());
    return quoteDao.saveAll(quotes);
  }

  /**
   * Helper method
   *
   * @param ticker
   * @return
   */
  public Quote saveQuote(String ticker) {
    Optional<IexQuote> savedIexQuote = marketDataDao.findById(ticker);
    if (savedIexQuote.isPresent()) {
      Quote savedQuote = buildQuoteFromIexQuote(savedIexQuote.get());
      return quoteDao.save(savedQuote);
    }
    return null;
  }

  /**
   * Find an IexQuote
   *
   * @param ticker
   * @return IexQuote object
   * @throws IllegalArgumentException if ticker is invalid
   */
  public IexQuote findIexQuoteByTicker(String ticker) {
    Optional<IexQuote> result = marketDataDao.findById(ticker);
    if (result.isPresent()) {
      return result.get();
    } else {
      return null;
    }
  }

  /**
   * Update a given quote to quote table without validation
   *
   * @param quote entity
   */
  public Quote saveQuote(Quote quote) {
    return quoteDao.save(quote);
  }

  /**
   * Find all quotes from the quote table
   *
   * @return a list of quotes
   */
  public List<Quote> findAllQuotes() {
    return quoteDao.findAll();
  }
}

