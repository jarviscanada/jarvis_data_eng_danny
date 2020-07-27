package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

  @Captor
  ArgumentCaptor<SecurityOrder> captorSecurityOrder;

  @Mock
  private AccountDao accountDao;
  @Mock
  private SecurityOrderDao securityOrderDao;
  @Mock
  private QuoteDao quoteDao;
  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private OrderService orderService;

  @Test
  public void executeBuyOrderTest() {
    MarketOrderDto orderDto = new MarketOrderDto();
    orderDto.setAccountId(1);
    orderDto.setSize(20);
    orderDto.setTicker("AAPL");

    Account testAccount = new Account();
    testAccount.setId(1);
    testAccount.setTraderId(1);
    testAccount.setAmount(500d);

    Quote testQuote = new Quote();
    testQuote.setAskPrice(5d);

    OrderService spyService = Mockito.spy(orderService);
    doReturn(Optional.of(testAccount)).when(accountDao).findById(any());
    doReturn(Optional.of(testQuote)).when(quoteDao).findById(any());
    SecurityOrder actualOrder = spyService.executeMarketOrder(orderDto);

    Assert.assertNotNull(actualOrder);
    Assert.assertEquals(testQuote.getAskPrice(), actualOrder.getPrice());
    Assert.assertEquals(orderDto.getSize(), actualOrder.getSize());
  }

  @Test
  public void failBuyOrderTest() {
    MarketOrderDto orderDto = new MarketOrderDto();
    orderDto.setAccountId(1);
    orderDto.setSize(null);
    orderDto.setTicker("AAPL");

    SecurityOrder order = new SecurityOrder();
    order.setPrice(50d);
    order.setTicker("AAPL");
    order.setSize(20);
    order.setStatus("FILLED");
    order.setAccountId(1);
    order.setId(1);

    OrderService spyService = Mockito.spy(orderService);
    doReturn(order).when(spyService).executeMarketOrder(any());
    SecurityOrder actualOrder = spyService.executeMarketOrder(orderDto);

    try {
      orderService.executeMarketOrder(orderDto);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    orderDto.setSize(20);
    orderDto.setTicker("AAAPL");

    try {
      orderService.executeMarketOrder(orderDto);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }
  }

  @Test
  public void executeSellOrderTest() {
    MarketOrderDto orderDto = new MarketOrderDto();
    orderDto.setAccountId(1);
    orderDto.setSize(-20);
    orderDto.setTicker("AAPL");

    Account testAccount = new Account();
    testAccount.setId(1);
    testAccount.setTraderId(1);
    testAccount.setAmount(500d);

    Quote testQuote = new Quote();
    testQuote.setBidPrice(5d);

    Position testPosition = new Position();
    testPosition.setAccountId(1);
    testPosition.setTicker(orderDto.getTicker());
    testPosition.setPosition(20);

    OrderService spyService = Mockito.spy(orderService);
    doReturn(Optional.of(testAccount)).when(accountDao).findById(any());
    doReturn(Optional.of(testQuote)).when(quoteDao).findById(any());
    doReturn(Optional.of(testPosition)).when(positionDao).findByIdAndTicker(any(), any());
    SecurityOrder actualOrder = spyService.executeMarketOrder(orderDto);

    Assert.assertNotNull(actualOrder);
    Assert.assertEquals(orderDto.getSize(), actualOrder.getSize());
  }
}
