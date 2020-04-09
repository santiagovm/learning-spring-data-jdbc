package pivotal.io.learningspringdatajdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataJdbc
@Component
public class FlightRepositoryTests {

    @Autowired
    private FlightRepository _repo;

    @BeforeEach
    void setup_before_each_test() {
        _repo.deleteAll();
    }

    @Test
    void should_perform_crud_operations() {

        Flight flight = new Flight();
        flight.destination = "New York";
        flight.origin = "Milan";
        flight.scheduledAt = Instant.parse("1999-12-31T11:59:59Z");

        _repo.save(flight);

        assertThat(_repo.findAll())
                .hasSize(1)
                .first()
                .isEqualToIgnoringGivenFields(flight, "createdDate", "lastModifiedDate");

        _repo.deleteById(flight.id);

        assertThat(_repo.count()).isZero();
    }

    @Test
    void should_find_flights_from_london() {
        final Flight flight0 = FlightFactory.create("London");
        final Flight flight1 = FlightFactory.create("London");
        final Flight flight2 = FlightFactory.create("New York");

        _repo.save(flight0);
        _repo.save(flight1);
        _repo.save(flight2);

        List<Flight> flightsFromLondon = _repo.findByOrigin("London");

        assertThat(flightsFromLondon).hasSize(2);

        assertThat(flightsFromLondon.get(0)).isEqualToIgnoringGivenFields(flight0, "createdDate", "lastModifiedDate");
        assertThat(flightsFromLondon.get(1)).isEqualToIgnoringGivenFields(flight1, "createdDate", "lastModifiedDate");
    }

    @Test
    void should_sort_flights_by_origin() {
        final Flight flight0 = FlightFactory.create("London");
        final Flight flight1 = FlightFactory.create("New York");
        final Flight flight2 = FlightFactory.create("London");

        _repo.save(flight0);
        _repo.save(flight1);
        _repo.save(flight2);

        final Iterable<Flight> sortedFlights = _repo.findAllSortedByOrigin();

        assertThat(sortedFlights).hasSize(3);

        final Iterator<Flight> iterator = sortedFlights.iterator();

        assertThat(iterator.next().origin).isEqualTo("London");
        assertThat(iterator.next().origin).isEqualTo("London");
        assertThat(iterator.next().origin).isEqualTo("New York");
    }

    @Test
    void should_page_results() {
        for (int i = 0; i < 50; i++) {
            _repo.save(FlightFactory.create(String.valueOf(i)));
        }

        final List<Flight> page = _repo.findAllPaginated(10, 5);

        assertThat(page.size()).isEqualTo(5);

        assertThat(page.get(0).origin).isEqualTo("10");
        assertThat(page.get(1).origin).isEqualTo("11");
        assertThat(page.get(2).origin).isEqualTo("12");
        assertThat(page.get(3).origin).isEqualTo("13");
        assertThat(page.get(4).origin).isEqualTo("14");
    }

    @Test
    void should_use_custom_implementation() {
        final Flight flightToDelete = FlightFactory.create("London");
        final Flight flightToKeep = FlightFactory.create("Paris");

        _repo.save(flightToDelete);
        _repo.save(flightToKeep);

        _repo.deleteByOrigin(flightToDelete.origin);

        assertThat(_repo.findAll())
                .hasSize(1)
                .first()
                .isEqualToIgnoringGivenFields(flightToKeep, "createdDate", "lastModifiedDate");
    }

    @Test
    void should_save_auditing_fields_to_database() {

        Flight flight = new Flight();
        flight.destination = "New York";
        flight.origin = "Milan";
        flight.scheduledAt = Instant.parse("1999-12-31T11:59:59Z");

        // assert audit fields are null before database save
        assertThat(flight.createdBy).isNull();
        assertThat(flight.createdDate).isNull();
        assertThat(flight.lastModifiedBy).isNull();
        assertThat(flight.lastModifiedDate).isNull();

        Flight savedFlight = _repo.save(flight);

        Flight flightFromDb1 = getFlightFromDatabase(savedFlight.id);

        // assert audit fields were set in database
        assertThat(flightFromDb1.createdBy).isEqualTo("the-current-user-that-owns-this-session");
        assertThat(flightFromDb1.lastModifiedBy).isEqualTo("the-current-user-that-owns-this-session");
        assertThat(flightFromDb1.createdDate).isNotNull();
        assertThat(flightFromDb1.lastModifiedDate).isEqualTo(flightFromDb1.createdDate);

        // copy timestamps before second save to compare later
        Instant createdDate1 = flightFromDb1.createdDate;
        Instant lastModifiedDate1 = flightFromDb1.lastModifiedDate;

        // saving to database a second time
        flightFromDb1.origin = "some new origin";
        _repo.save(flightFromDb1);

        Flight flightFromDb2 = getFlightFromDatabase(savedFlight.id);

        // assert that last modified date changed
        assertThat(flightFromDb2.createdDate).isEqualTo(createdDate1);
        assertThat(flightFromDb2.lastModifiedDate).isAfter(lastModifiedDate1);
    }

    @Test
    void should_use_entity_callback_to_change_destination_name() {

        Flight flight = new Flight();
        flight.destination = "New York";
        flight.origin = "Milan";
        flight.scheduledAt = Instant.parse("1999-12-31T11:59:59Z");

        // assert destination is not changed yet
        assertThat(flight.destination).isEqualTo("New York");

        Flight savedFlight = _repo.save(flight);

        Flight flightFromDb1 = getFlightFromDatabase(savedFlight.id);

        // assert different destination was saved
        assertThat(flightFromDb1.destination).isEqualTo("foo-New York");
    }

    private Flight getFlightFromDatabase(Long id) {
        Optional<Flight> optionalFlightFromDb1 = _repo.findById(id);
        assertThat(optionalFlightFromDb1.isPresent()).isTrue();
        return optionalFlightFromDb1.get();
    }

}
