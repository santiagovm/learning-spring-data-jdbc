package pivotal.io.learningspringdatajdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<PurchaseOrder, Long> {

    @Query("select count(*) from order_item")
    int countItems();
}
