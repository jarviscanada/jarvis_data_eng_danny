package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping(path = "/marketOrder")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public SecurityOrder postMarketOrder(@RequestBody MarketOrderDto orderDto) {
    try {
      return orderService.executeMarketOrder(orderDto);
    } catch (Exception ex) {
      throw ResponseExceptionUtil.getResponseStatusException(ex);
    }
  }
}
