package eg.gov.iti.jets.petstore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ServiceRate {
    @EmbeddedId
    private ServiceRateId id;
    @ManyToOne(optional = false)
    @MapsId("customerId")
    private Customer customer;
    @ManyToOne(optional = false)
    @MapsId("serviceId")
    private Service service;
    @Column(name = "rate_number", nullable = false, length = 5)
    private Integer rateNumber;
}
