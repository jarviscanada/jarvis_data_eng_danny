package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {

  private final TraderDao traderDao;
  private final AccountDao accountDao;
  private final PositionDao positionDao;
  private final SecurityOrderDao securityOrderDao;

  @Autowired
  public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
      PositionDao positionDao, SecurityOrderDao securityOrderDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.securityOrderDao = securityOrderDao;
  }

  /**
   * Create a new trader and initialize a new account with 0 amount. - validate user input (all
   * fields must be non-empty) - create a trader - create an account - create, setup, and return a
   * new traderAccountView Assumption: to simplify the logic, each trader has  only one account
   * where traderId == accountId
   *
   * @param trader cannot be null. All fields cannot be null except for id (auto-generated by db)
   * @return traderAccountView
   * @throws IllegalArgumentException if a trader has null fields or id is not null
   */
  public TraderAccountView createTraderAndAccount(Trader trader) {
    Account account = new Account();
    account.setAmount(0d);

    Trader newTrader = traderDao.save(trader);
    account.setId(newTrader.getId());
    account.setTraderId(newTrader.getId());
    accountDao.save(account);

    TraderAccountView view = new TraderAccountView();
    view.setTrader(trader);
    view.setAccount(account);

    return view;
  }

  /**
   * A Trader can be deleted iff it has no open position and 0 cash balance. - validate traderId -
   * get trader account by traderId and check account balance - get positions by accountId and check
   * positions - delete all securityOrders, accounts, trader (in this order)
   *
   * @param traderId must not be null
   * @throws IllegalArgumentException if traderId is not found or null or unable to delete
   */
  public void deleteTraderById(Integer traderId) {
    if (traderId <= 0 || traderId == null) {
      throw new IllegalArgumentException("Invalid Trader ID");
    }

    Trader trader = traderDao.findById(traderId).orElseThrow(
        () -> new IllegalArgumentException("No trader with that ID exists"));
    Account account = accountDao.findById(traderId).orElse(new Account());
    clearAccount(account);

    securityOrderDao.deleteByAccountId(account.getId());
    accountDao.deleteById(account.getId());
    traderDao.deleteById(traderId);
  }

  /**
   * Helper method that throws exception if an account is NOT clear to be deleted
   *
   * @param account
   * @throws IllegalArgumentException if account has above 0 balance or open positions
   */
  private void clearAccount(Account account) throws IllegalArgumentException {

    if (account.getAmount() > 0.0d) {
      throw new IllegalArgumentException();
    }

    List<Position> positionList = positionDao.findById(account.getId());
    for (Position position : positionList) {
      if (position.getPosition() > 0) {
        throw new IllegalArgumentException();
      }
    }
  }

  /**
   * Deposit a fund to an account by traderId - validate user input - account =
   * accountDao.findByTraderId - accountDao.updateOne
   *
   * @param traderId must not be null
   * @param fund     must be greater than 0
   * @return updated Account
   * @throws IllegalArgumentException if traderId is null or not found, and fund is less or equal to
   *                                  0
   */
  public Account deposit(Integer traderId, Double fund) {
    if (traderId <= 0 || traderId == null) {
      throw new IllegalArgumentException("Invalid Trader ID");
    } else if (fund <= 0.0d) {
      throw new IllegalArgumentException("Deposit amount must be greater than 0");
    }

    Account account = accountDao.findById(traderId).
        orElseThrow(() -> new IllegalArgumentException("No account found with that ID"));
    account.setAmount(account.getAmount() + fund);
    accountDao.updateOne(account);
    return account;
  }

  /**
   * Withdraw a fund to an account by traderId
   * <p>
   * - validate user input - account = accountDao.findByTraderId - accountDao.updateAmountById
   *
   * @param traderId trader ID
   * @param fund     amount can't be 0
   * @return updated Account
   * @throws IllegalArgumentException if traderId is null or not found, fund is less or equal to 0,
   *                                  and insufficient funds
   */
  public Account withdraw(Integer traderId, Double fund) {
    if (traderId <= 0 || traderId == null) {
      throw new IllegalArgumentException("Invalid Trader ID");
    } else if (fund <= 0.0d) {
      throw new IllegalArgumentException("Withdraw amount must be greater than 0");
    }

    Account account = accountDao.findById(traderId).
        orElseThrow(() -> new IllegalArgumentException("No account found with that ID"));

    Double newAmount = account.getAmount() - fund;
    if (newAmount < 0.0d) {
      throw new IllegalArgumentException("Insufficient funds to withdraw");
    }
    account.setAmount(newAmount);
    accountDao.updateOne(account);
    return account;
  }
}