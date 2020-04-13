package pivotal.io.learningspringdatajdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DeleteByOriginRepositoryImpl implements DeleteByOriginRepository {

    private final JdbcTemplate _jdbcTemplate;

    public DeleteByOriginRepositoryImpl(DataSource dataSource) {
        _jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void deleteByOrigin(String origin) {
        _jdbcTemplate.update("delete from flight where origin = ?", origin);
    }
}
