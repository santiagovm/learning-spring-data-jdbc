package pivotal.io.learningspringdatajdbc;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.mapping.event.RelationalEvent;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories
@EnableJdbcAuditing
public class AppConfiguration extends AbstractJdbcConfiguration {

    @Bean
    DataSource createDataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .addScript("create-db-schema.sql")
                .build();
    }

    @Bean
    public AuditorAware<String> createAuditorAware() {
        return new AuditorAwareStub();
    }

    @Bean
    public ApplicationListener<?> createLoggingListener() {
        return (ApplicationListener<ApplicationEvent>) event -> {
            if (event instanceof RelationalEvent) {
                System.out.println(" >>>>> detected a relational event: " + event);
            }
        };
    }

    @Bean
    public BeforeSaveCallback<Flight> createBeforeSaveCallbackForFlights() {
        return (entity, aggregateChange) -> {
            entity.destination = "foo-" + entity.destination;
            return entity;
        };
    }

}
