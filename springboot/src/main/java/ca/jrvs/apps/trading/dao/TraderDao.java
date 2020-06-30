package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.Trader;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class TraderDao extends JdbcCrudDao<Trader> {

  private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);
  private final String TABLE_NAME = "trader";
  private final String ID_COLUMN_NAME = "id";
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public TraderDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN_NAME);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() { return simpleJdbcInsert; }

  @Override
  public String getTableName() { return TABLE_NAME; }

  @Override
  public String getIdColumnName() { return ID_COLUMN_NAME; }

  @Override
  Class<Trader> getEntityClass() { return Trader.class; }

  @Override
  public int updateOne(Trader trader) {
    String updateSql = "UPDATE trader SET first_name=?, last_name=?, dob=?, country=?, email=?"
        + " WHERE id=?";
    return jdbcTemplate.update(updateSql, makeUpdateValues(trader));
  }

  public Trader save(Trader trader) {
    if (existsById(trader.getId())) {
      int updateRowNo = updateOne(trader);
      if (updateRowNo != 1) {
        throw new DataRetrievalFailureException("Unable to update trader");
      }
    } else {
      addOne(trader);
    }
    return trader;
  }

  public void addOne(Trader trader) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(trader);
    int row = simpleJdbcInsert.execute(parameterSource);
    if (row != 1) {
      throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
    }
  }

  public boolean existsById(Integer id) {
    String selectSql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME
        + "=?";
    int count = jdbcTemplate.queryForObject(selectSql, new Object[]{id}, Integer.class);

    return count > 0;
  }

  public Object[] makeUpdateValues(Trader trader) {
    List<Object> args = new ArrayList<>();
    args.add(trader.getFirstName());
    args.add(trader.getLastName());
    args.add(trader.getCountry());
    args.add(trader.getDob());
    args.add(trader.getEmail());
    args.add(trader.getId());
    return args.toArray();
  }

  @Override
  public <S extends Trader> Iterable<S> saveAll(Iterable<S> iterable) {
    return (List<S>) StreamSupport.stream(iterable.spliterator(), false)
        .map(trader -> save(trader)).collect(Collectors.toList());
  }

  @Override
  public Optional<Trader> findById(Integer id) {
    Trader trader = null;
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";

    try {
      trader= jdbcTemplate.queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(Trader.class), id);
    } catch (EmptyResultDataAccessException ex) {
      logger.error("Empty result on findById: " + id, ex);
    }

    if (trader == null) {
      return Optional.empty();
    }
    return Optional.of(trader);
  }

  @Override
  public Iterable<Trader> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(Trader.class));
  }

  @Override
  public void deleteById(Integer integer) {
    String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
    jdbcTemplate.update(deleteSql, integer);
  }

  @Override
  public long count() {
    String countSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
    return jdbcTemplate.queryForObject(countSql, Long.class);
  }
}
