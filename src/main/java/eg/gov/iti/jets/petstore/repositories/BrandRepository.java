package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
