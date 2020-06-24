package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_COLUMN_NAME = "ticker";

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public QuoteDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
  }

  /**
   * Save quote to db
   *
   * @param quote
   * @return Quote object
   * @throws org.springframework.dao.DataAccessException for unexpected SQL result or execution
   *                                                     failure
   */
  @Override
  public Quote save(Quote quote) {
    if (existsById(quote.getTicker())) {
      int updateRowNo = updateOne(quote);
      if (updateRowNo != 1) {
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    } else {
      addOne(quote);
    }
    return quote;
  }

  /**
   * helper for saving one Quote
   *
   * @param quote
   */
  private void addOne(Quote quote) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
    int row = simpleJdbcInsert.execute(parameterSource);
    if (row != 1) {
      throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
    }
  }

  /**
   * helper for updating one Quote
   *
   * @param quote
   * @return
   */
  private int updateOne(Quote quote) {
    String updateSql = "UPDATE quote SET last_price=?, bid_price=?,"
        + "bid_size=?, ask_price=?, ask_size=? WHERE ticker=?";
    return jdbcTemplate.update(updateSql, makeUpdateValues(quote));
  }

  /**
   * helper method that makes the values for an SQL update
   *
   * @param quote
   * @return update values
   */
  private Object[] makeUpdateValues(Quote quote) {
    List<Object> args = new ArrayList<>();
    args.add(quote.getLastPrice());
    args.add(quote.getBidPrice());
    args.add(quote.getBidSize());
    args.add(quote.getAskPrice());
    args.add(quote.getAskSize());
    args.add(quote.getTicker());
    Object[] argsArray = args.toArray();
    return argsArray;
  }

  @Override
  public <S extends Quote> List<S> saveAll(Iterable<S> iterable) {
    return (List<S>) StreamSupport.stream(iterable.spliterator(), false)
        .map(quote -> save(quote)).collect(Collectors.toList());
  }

  /**
   * Find a quote by ticker
   *
   * @param s
   * @return quote or Optional.empty if not found
   */
  @Override
  public Optional<Quote> findById(String s) {
    Quote quote = null;
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME
        + "=?";

    try {
      quote = jdbcTemplate.queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(Quote.class), s);
    } catch (EmptyResultDataAccessException ex) {
      logger.error("Empty result on findById: " + s, ex);
    }
    if (quote == null) {
      return Optional.empty();
    }
    return Optional.of(quote);
  }

  @Override
  public boolean existsById(String s) {
    String selectSql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME
        + "=?";
    int count = jdbcTemplate.queryForObject(selectSql, new Object[]{s}, Integer.class);

    return count > 0;
  }

  /**
   * return all quotes
   *
   * @return
   * @throws org.springframework.dao.DataAccessException if failed to update
   */
  @Override
  public List<Quote> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    List<Quote> quotes = jdbcTemplate.query(selectSql,
        BeanPropertyRowMapper.newInstance(Quote.class));
    return quotes;
  }

  @Override
  public long count() {
    String countSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
    return jdbcTemplate.queryForObject(countSql, Long.class);
  }

  @Override
  public void deleteById(String s) {
    String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
    jdbcTemplate.update(deleteSql, s);
  }

  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + TABLE_NAME;
    jdbcTemplate.update(deleteSql);
  }

  @Override
  public Iterable<Quote> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(Quote quote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
