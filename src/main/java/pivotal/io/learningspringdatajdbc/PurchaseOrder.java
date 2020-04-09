package pivotal.io.learningspringdatajdbc;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

public class PurchaseOrder {
    @Id
    Long id;
    String shippingAddress;
    Set<OrderItem> items = new HashSet<>();

    void addItem(int quantity, String product) {
        items.add(createOrderItem(quantity, product));
    }

    private OrderItem createOrderItem(int quantity, String product) {
        OrderItem item = new OrderItem();
        item.product = product;
        item.quantity = quantity;
        return item;
    }
}
