package pivotal.io.learningspringdatajdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Component
public class TransactionalTests {

    @Autowired
    private FlightRepository _repo;

    @Autowired
    private FlightService _service;

    @BeforeEach
    void setup_before_each_test() {
        _repo.deleteAll();
    }

    @Test
    void should_not_rollback_when_there_is_not_transaction() {
        try {
            _service.saveFlight(FlightFactory.create("Barcelona"));
        } catch (Exception e) {
            // empty
        }
        finally {
            assertThat(_repo.findAll()).isNotEmpty();
        }
    }

    @Test
    void should_rollback_when_there_is_a_transaction() {
        try {
            _service.saveFlightTransactional(FlightFactory.create("Barcelona"));
        } catch (Exception e) {
            // empty
        }
        finally {
            assertThat(_repo.findAll()).isEmpty();
        }
    }
}
