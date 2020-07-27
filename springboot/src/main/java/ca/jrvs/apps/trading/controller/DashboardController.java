package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.PortfolioView;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import ca.jrvs.apps.trading.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/profile/traderId/{traderId}", produces = {
      MediaType.APPLICATION_JSON_UTF8_VALUE})
  public TraderAccountView getAccount(@PathVariable Integer traderId) {
    try {
     return dashboardService.getTraderAccount(traderId);
    } catch(Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }

  @GetMapping(path = "/portfolio/traderId/{traderId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PortfolioView getPortfolioView(@PathVariable Integer traderId) {
    try {
      return dashboardService.getProfileViewByTraderId(traderId);
    } catch(Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }
}
