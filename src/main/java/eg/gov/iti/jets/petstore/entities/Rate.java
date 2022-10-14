package eg.gov.iti.jets.petstore.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Rate {
    @EmbeddedId
    private RateId id;
    @ManyToOne(optional = false)
    @MapsId("userId")
    private Customer user;
    @ManyToOne(optional = false)
    @MapsId("productId")
    private Product product;
    @Column(name = "rate_number", nullable = false, length = 5)
    private Integer rateNumber;
}
