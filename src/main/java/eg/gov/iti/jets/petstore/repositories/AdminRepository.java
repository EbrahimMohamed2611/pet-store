package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
