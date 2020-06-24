package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.service.QuoteService;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {JdbcTemplateAutoConfiguration.class,
    DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(Application.class);

//  @Value("${app.init.dailyList}")
//  private String[] initDailyList;

  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteController quoteController;

  public static void main(String[] args) {
    BasicConfigurator.configure();
    SpringApplication app = new SpringApplication(Application.class);
    app.run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Run");
    quoteController.getQuote(args[0]);
  }
}
