package eg.gov.iti.jets.petstore.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;


@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ROLE_SERVICE_PROVIDER")
@Getter
@Setter
public class ServiceProvider extends User {
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Service> services = new HashSet<>(0);
}
