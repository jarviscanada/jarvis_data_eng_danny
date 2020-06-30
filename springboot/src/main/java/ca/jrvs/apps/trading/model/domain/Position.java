package ca.jrvs.apps.trading.model.domain;

public class Position {

  private Integer accountId;
  private String ticker;
  private Integer sumOfSize;

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public Integer getSumOfSize() {
    return sumOfSize;
  }

  public void setSumOfSize(Integer sumOfSize) {
    this.sumOfSize = sumOfSize;
  }
}
