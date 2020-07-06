package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
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
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

  private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);

  private final String TABLE_NAME = "security_order";
  private final String ID_COLUMN_NAME = "id";
  private final String ACCOUNT_ID_COLUMN = "account_id";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public SecurityOrderDao(DataSource dataSource) {
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
  Class<SecurityOrder> getEntityClass() {
    return SecurityOrder.class;
  }

  @Override
  public int updateOne(SecurityOrder order) {
    String updateSql = "UPDATE security_order SET account_id=?, status=?, ticker=?, size=?, "
        + "price=?, notes=? WHERE id=?";
    return jdbcTemplate.update(updateSql, makeUpdateValues(order));
  }

  public SecurityOrder save(SecurityOrder order) {
    if (existsById(order.getId())) {
      int updatedRowNo = updateOne(order);
      if (updatedRowNo != 1) {
        throw new DataRetrievalFailureException("Unable to update security order");
      }
    } else {
      addOne(order);
    }
    return order;
  }

  public void addOne(SecurityOrder order) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(order);
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

  public Object[] makeUpdateValues(SecurityOrder order) {
    List<Object> args = new ArrayList<>();
    args.add(order.getAccountId());
    args.add(order.getStatus());
    args.add(order.getTicker());
    args.add(order.getSize());
    args.add(order.getPrice());
    args.add(order.getNotes());
    args.add(order.getId());
    return args.toArray();
  }

  @Override
  public <S extends SecurityOrder> Iterable<S> saveAll(Iterable<S> iterable) {
    return (List<S>) StreamSupport.stream(iterable.spliterator(), false)
        .map(order -> save(order)).collect(Collectors.toList());
  }

  @Override
  public Optional<SecurityOrder> findById(Integer id) {
    SecurityOrder order = null;
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";

    try {
      order = jdbcTemplate.queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(SecurityOrder.class), id);
    } catch (EmptyResultDataAccessException ex) {
      logger.error("Empty result on findById: " + id, ex);
    }

    if (order == null) {
      return Optional.empty();
    }
    return Optional.of(order);
  }

  @Override
  public Iterable<SecurityOrder> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(SecurityOrder.class));
  }

  @Override
  public void deleteById(Integer integer) {
    String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
    jdbcTemplate.update(deleteSql, integer);
  }

  public void deleteByAccountId(Integer integer) {
    String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ACCOUNT_ID_COLUMN + "=?";
    jdbcTemplate.update(deleteSql, integer);
  }

  @Override
  public long count() {
    String countSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
    return jdbcTemplate.queryForObject(countSql, Long.class);
  }
}
