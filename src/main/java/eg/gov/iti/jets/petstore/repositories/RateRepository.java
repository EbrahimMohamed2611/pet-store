package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Rate;
import eg.gov.iti.jets.petstore.entities.RateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, RateId> {
}
