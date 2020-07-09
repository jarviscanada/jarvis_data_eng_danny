package ca.jrvs.apps.trading.model.domain;

public class SecurityRow {

  private Position position;
  private Quote quote;
  private String ticker;

  public SecurityRow() {}

  public SecurityRow(Position position, Quote quote, String ticker) {
    this.position = position;
    this.quote = quote;
    this.ticker = ticker;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Quote getQuote() {
    return quote;
  }

  public void setQuote(Quote quote) {
    this.quote = quote;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }
}
