package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/quote")
public class QuoteController {

  private final QuoteService quoteService;

  @Autowired
  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  @GetMapping(path = "/iex/ticker/{ticker}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public IexQuote getQuote(@PathVariable String ticker) {
    try {
      return quoteService.findIexQuoteById(ticker);
    } catch (Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }

  @PutMapping(path = "/iexMarketData")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Quote> updateMarketData() {
    try {
      return quoteService.updateMarketData();
    } catch (Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }

  @PutMapping(path = "/")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Quote putQuote(@RequestBody Quote quote) {
    try {
      return quoteService.saveQuote(quote);
    } catch (Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }

  @PostMapping(path = "/tickerId/{tickerId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Quote createQuote(@PathVariable String tickerId) {
    try {
      return quoteService.saveQuote(tickerId);
    } catch (Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }

  @GetMapping(path = "/dailyList")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Quote> getDailyList() {
    try {
      return quoteService.findAllQuotes();
    } catch (Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }
}