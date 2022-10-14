package eg.gov.iti.jets.petstore.entities;

import eg.gov.iti.jets.petstore.enums.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORDER_INFO")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "purchase_date")
    private LocalDateTime date = LocalDateTime.now();
    private Address address;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItems> items = new HashSet<>(0);
    private String customerNotes;
}
