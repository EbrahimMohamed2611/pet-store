package eg.gov.iti.jets.petstore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("ROLE_CUSTOMER")
@Getter
@Setter
public class Customer extends User {
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    private Set<CartItem> shoppingCart;


}
