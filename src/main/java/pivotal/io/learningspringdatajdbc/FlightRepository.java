package pivotal.io.learningspringdatajdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface FlightRepository extends CrudRepository<Flight, Long>, DeleteByOriginRepository {

    // Spring Data JDBC does not support derived queries yet. Query must be added using @Query annotation
    @Query("select id, origin, destination, scheduled_at, created_by, created_date, last_modified_by, last_modified_date "
            + "from flight "
            + "where origin = :origin")
    List<Flight> findByOrigin(@Param("origin") String origin);

    @Query("select id, origin, destination, scheduled_at, created_by, created_date, last_modified_by, last_modified_date "
            + "from flight "
            + "order by origin")
    List<Flight> findAllSortedByOrigin();

    @Query("select id, origin, destination, scheduled_at, created_by, created_date, last_modified_by, last_modified_date "
            + "from flight "
            + "order by id "
            + "limit :limit offset :offset")
    List<Flight> findAllPaginated(@Param("offset") int offset, @Param("limit") int limit);
}
