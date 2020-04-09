package pivotal.io.learningspringdatajdbc;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FlightService {

    private final FlightRepository _repo;

    public FlightService(FlightRepository repo) {
        _repo = repo;
    }

    public void saveFlight(Flight flight) {
        _repo.save(flight);
        throw new RuntimeException("something very bad just happened");
    }

    @Transactional
    public void saveFlightTransactional(Flight flight) {
        _repo.save(flight);
        throw new RuntimeException("something very bad just happened");
    }
}
