package eg.gov.iti.jets.petstore.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItems {
    public OrderItems(Integer quantity, Float priceAfterDiscount, Product product, Order order) {
        this.quantity = quantity;
        this.priceAfterDiscount = priceAfterDiscount;
        this.product = product;
        this.order = order;
    }

    @EmbeddedId
    private OrderItemsId id = new OrderItemsId();
    private Integer quantity;
    @Column(name = "price_after_discount")
    private Float priceAfterDiscount;
    @ManyToOne
    @MapsId("orderId")
    private Order order;
    @ManyToOne
    @MapsId("productId")
    private Product product;

}
