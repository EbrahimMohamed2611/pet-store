package eg.gov.iti.jets.petstore.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRateId implements Serializable {
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "service_id")
    private Long serviceId;
}
