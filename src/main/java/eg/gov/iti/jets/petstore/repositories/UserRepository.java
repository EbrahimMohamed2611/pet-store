package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);

}
