package pivotal.io.learningspringdatajdbc;

import java.time.Instant;

public class FlightFactory {

    public static Flight create(String origin) {
        Flight flight = new Flight();

        flight.destination = "Madrid";
        flight.origin = origin;
        flight.scheduledAt = Instant.parse("1999-12-31T11:59:59Z");

        return flight;
    }
}
