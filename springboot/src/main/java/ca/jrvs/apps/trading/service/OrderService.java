package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private QuoteDao quoteDao;
  private PositionDao positionDao;

  private final String tickerRegex = "[a-zA-Z]{1,5}";

  @Autowired
  public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
      QuoteDao quoteDao, PositionDao positionDao) {
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
  }

  /**
   * Execute a market order
   * - validate the order (e.g. size and ticker)
   * - create a SecurityOrder (for security_order table)
   * - handle buy or sell order
   *  - buy order: check account balance (calls helper method)
   *  - sell order: check position for the ticker/symbol (calls helper method)
   *  - (please don't forget to update SecurityOrder.status)
   * - save and return SecurityOrder
   *
   * @param orderDto market order
   * @return SecurityOrder from security_order table
   * @throws org.springframework.dao.DataAccessException if unable to get  data from DAO
   * @throws IllegalArgumentException for invalid input
   */
  public SecurityOrder executeMarketOrder(MarketOrderDto orderDto) {
    validateOrder(orderDto);
    SecurityOrder securityOrder = createSecurityOrder(orderDto);
    Account account = accountDao.findById(orderDto.getAccountId()).orElseThrow(
        () -> new IllegalArgumentException("No account found with that ID"));

    if (orderDto.getSize() > 0) {
      handleBuyMarketOrder(orderDto, securityOrder, account);
    } else if (orderDto.getSize() < 0) {
      handleSellMarketOrder(orderDto, securityOrder, account);
    }
    return securityOrder;
  }

  /**
   * Helper method to validate market order
   * @throws IllegalArgumentException if order is invalid size or ticker can't be found
   */
  private void validateOrder(MarketOrderDto orderDto) {
    if (orderDto.getSize() == null || orderDto.getSize() == 0) {
      throw new IllegalArgumentException("Error: Invalid order size.");
    } else if (!orderDto.getTicker().matches(tickerRegex)) {
      throw new IllegalArgumentException("Error: Illegal ticker format.");
    }
  }

  /**
   * Helper method to create a SecurityOrder from MarketDataDto
   * @param orderDto
   * @return SecurityOrder
   */
  private SecurityOrder createSecurityOrder(MarketOrderDto orderDto) {
    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setAccountId(orderDto.getAccountId());
    securityOrder.setTicker(orderDto.getTicker());
    securityOrder.setSize(orderDto.getSize());
    securityOrder.setStatus("PENDING");
    return securityOrder;
  }

  /**
   * Helper method to execute buy order
   * @param orderDto user order
   * @param securityOrder to be saved into database
   * @param account account
   */
  public void handleBuyMarketOrder(MarketOrderDto orderDto, SecurityOrder securityOrder,
      Account account) {
    String ticker = orderDto.getTicker();
    Quote quote = quoteDao.findById(ticker).orElseThrow(
        () -> new IllegalArgumentException("Error retrieving quote for: " + ticker));

    Double orderAmount = quote.getAskPrice() * orderDto.getSize();
    if (account.getAmount() < orderAmount) {
      throw new IllegalArgumentException("Insufficient funds for order.");
    }

    account.setAmount(account.getAmount() - orderAmount);
    securityOrder.setPrice(quote.getAskPrice());
    securityOrder.setStatus("FILLED");
    accountDao.save(account);
    securityOrderDao.save(securityOrder);
  }

  /**
   * Helper method to execute sell order
   * @param orderDto user order
   * @param securityOrder to be saved into database
   * @param account account
   */
  public void handleSellMarketOrder(MarketOrderDto orderDto, SecurityOrder securityOrder,
      Account account) {
    String ticker = orderDto.getTicker();
    Position position = positionDao.findByIdAndTicker(account.getId(), ticker).orElseThrow(
        () -> new IllegalArgumentException("Account has no such position to sell."));
    Quote quote = quoteDao.findById(ticker).orElseThrow(
        () -> new IllegalArgumentException("Error retrieving quote for: " + ticker));

    if (position.getPosition() < orderDto.getSize()) {
      throw new IllegalArgumentException("Sell order size exceeds existing position.");
    }

    Double orderAmount = quote.getBidPrice() * Math.abs(orderDto.getSize());
    account.setAmount(orderAmount);
    securityOrder.setPrice(orderAmount);
    securityOrder.setStatus("FILLED");
    accountDao.updateOne(account);
    securityOrderDao.save(securityOrder);
  }
}
