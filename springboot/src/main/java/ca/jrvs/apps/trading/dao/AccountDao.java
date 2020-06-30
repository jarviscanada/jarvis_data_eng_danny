package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
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
public class AccountDao extends JdbcCrudDao<Account> {

  private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

  private final String TABLE_NAME = "account";
  private final String ID_COLUMN_NAME = "id";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public AccountDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN_NAME);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return simpleJdbcInsert;
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public String getIdColumnName() {
    return ID_COLUMN_NAME;
  }

  @Override
  Class<Account> getEntityClass() {
    return Account.class;
  }

  @Override
  public int updateOne(Account account) {
    String updateSql = "UPDATE account SET amount=? WHERE id=?";
    return jdbcTemplate.update(updateSql, makeUpdateValues(account));
  }

  public Account save(Account account) {
    if (existsById(account.getId())) {
      int updatedRowNo = updateOne(account);
      if (updatedRowNo != 1) {
        throw new DataRetrievalFailureException("Unable to update account");
      }
    } else {
      addOne(account);
    }
    return account;
  }

  public void addOne(Account account) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(account);
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

  public Object[] makeUpdateValues(Account account) {
    List<Object> args = new ArrayList<>();
    args.add(account.getAmount());
    args.add(account.getId());
    return args.toArray();
  }

  @Override
  public <S extends Account> Iterable<S> saveAll(Iterable<S> iterable) {
    return (List<S>) StreamSupport.stream(iterable.spliterator(), false)
        .map(account -> save(account)).collect(Collectors.toList());
  }

  @Override
  public Optional<Account> findById(Integer id) {
    Account account = null;
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";

    try {
      account = jdbcTemplate.queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(Account.class), id);
    } catch (EmptyResultDataAccessException ex) {
      logger.error("Empty result on findById: " + id, ex);
    }

    if (account == null) {
      return Optional.empty();
    }
    return Optional.of(account);
  }

  @Override
  public Iterable<Account> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(Account.class));
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
