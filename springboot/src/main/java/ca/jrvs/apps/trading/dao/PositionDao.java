package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao {

  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);

  private final String TABLE_NAME = "position";
  private final String ID_COLUMN_NAME = "account_id";
  private final String TICKER_COLUMN_NAME = "ticker";

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Autowired
  public PositionDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public List<Position> findById(Integer id) {
    List<Position> positions = new ArrayList<>();
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";

    positions = jdbcTemplate.query(selectSql,
        BeanPropertyRowMapper.newInstance(Position.class), id);

    return positions;
  }

  public Optional<Position> findByIdAndTicker(Integer id, String ticker) {
    Position position = null;
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE (" + ID_COLUMN_NAME + "=? AND "
        + TICKER_COLUMN_NAME + "=?)";
    try {
      position = jdbcTemplate.queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(Position.class), id, ticker);
    } catch (EmptyResultDataAccessException ex) {
      logger.error("Empty result on findById: " + id, ex);
    }

    if (position == null) {
      return Optional.empty();
    }
    return Optional.of(position);
  }

  public Iterable<Position> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(Position.class));
  }

  public long count() {
    String countSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
    return jdbcTemplate.queryForObject(countSql, Long.class);
  }
}
