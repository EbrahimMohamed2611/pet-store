package eg.gov.iti.jets.petstore.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("ROLE_SELLER")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Seller extends User {
    @OneToMany(mappedBy = "seller")
    private Set<Product> products;
}
