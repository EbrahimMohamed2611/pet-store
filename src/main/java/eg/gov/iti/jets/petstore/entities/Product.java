package eg.gov.iti.jets.petstore.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private String description;
    private Float price;
    private Integer quantity;
    @ManyToOne
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images;
    @ManyToOne
    private Seller seller;
    private Boolean available = false;
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private Species species;
    private Float discount;
    @OneToMany(mappedBy = "product")
    private Set<Rate> rates;
    @OneToMany(mappedBy = "product")
    private Set<OrderItems> orderItems;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(price, product.price) && Objects.equals(quantity, product.quantity) && Objects.equals(available, product.available) && Objects.equals(discount, product.discount) && Objects.equals(creationDate, product.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, quantity, available, discount, creationDate);
    }
}
