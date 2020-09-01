package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.PortfolioView;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityRow;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardService {

  private final AccountDao accountDao;
  private final TraderDao traderDao;
  private final QuoteDao quoteDao;
  private final PositionDao positionDao;

  @Autowired
  public DashboardService(AccountDao accountDao, TraderDao traderDao, QuoteDao quoteDao,
      PositionDao positionDao) {
    this.accountDao = accountDao;
    this.traderDao = traderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
  }

  /**
   * Create and return a TraderAccountView by trader ID - get trader account by ID - get trader info
   * by ID - create and return a TraderAccountView
   *
   * @param traderId must not be null
   * @return TraderAccountView
   * @throws IllegalArgumentException if trader ID is null or not found
   */
  public TraderAccountView getTraderAccount(Integer traderId) {
    if (traderId == null || traderId == 0) {
      throw new IllegalArgumentException("Trader ID cannot be null or 0.");
    }

    Trader trader = traderDao.findById(traderId).orElseThrow(
        () -> new IllegalArgumentException("Trader ID not found"));
    Account account = accountDao.findById(traderId).orElseThrow(
        () -> new IllegalArgumentException("Account ID not found"));

    TraderAccountView accountView = new TraderAccountView();
    accountView.setTrader(trader);
    accountView.setAccount(account);
    return accountView;
  }

  /**
   * Create and return PortfolioView by ID - get account by trader ID - get positions by account ID
   * - create and return a PortfolioView
   *
   * @param traderId must not be null
   * @return PortfolioView
   * @throws IllegalArgumentException if traderId is null or not found
   */
  public PortfolioView getProfileViewByTraderId(Integer traderId) {
    Account account = accountDao.findById(traderId).orElseThrow(
        () -> new IllegalArgumentException("Account ID not found"));
    List<Position> positions = positionDao.findById(traderId);
    List<SecurityRow> securityRows = new ArrayList<>();

    for (Position position : positions) {
      String ticker = position.getTicker();
      Quote quote = quoteDao.findById(ticker).orElseThrow(
          () -> new DataRetrievalFailureException("Error retrieving quote by ticker."));
      securityRows.add(new SecurityRow(position, quote, ticker));
    }

    PortfolioView portfolioView = new PortfolioView();
    portfolioView.setSecurityRows(securityRows);
    return portfolioView;
  }
}
