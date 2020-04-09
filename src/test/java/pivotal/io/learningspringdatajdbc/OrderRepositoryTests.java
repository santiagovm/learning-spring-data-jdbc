package pivotal.io.learningspringdatajdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Component
public class OrderRepositoryTests {

    @Autowired
    OrderRepository _repo;

    @Test
    void should_perform_crud_operations_on_aggregate() {

        PurchaseOrder order = new PurchaseOrder();
        order.shippingAddress = "10 Main St, Springfield, MI 22222";
        order.addItem(4, "Captain Future Comet Lego Set");
        order.addItem(2, "Cute blue angler fish plush toy");

        PurchaseOrder savedOrder = _repo.save(order);

        assertThat(_repo.count()).isEqualTo(1);
        assertThat(_repo.countItems()).isEqualTo(2);

        _repo.delete(savedOrder);

        assertThat(_repo.count()).isEqualTo(0);
        assertThat(_repo.countItems()).isEqualTo(0);
    }
}
