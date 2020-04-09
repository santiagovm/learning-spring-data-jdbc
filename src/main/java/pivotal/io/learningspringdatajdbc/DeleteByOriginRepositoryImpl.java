package pivotal.io.learningspringdatajdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DeleteByOriginRepositoryImpl implements DeleteByOriginRepository {

    private final DataSource _dataSource;

    public DeleteByOriginRepositoryImpl(DataSource dataSource) {
        _dataSource = dataSource;
    }

    @Override
    public void deleteByOrigin(String origin) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(_dataSource);
        jdbcTemplate.update("delete from flight where origin = ?", origin);
    }
}
