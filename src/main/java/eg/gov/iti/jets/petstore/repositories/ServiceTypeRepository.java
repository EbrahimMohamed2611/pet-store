package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}
