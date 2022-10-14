package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    Set<Order> getOrderByUserId(Long userId);
}
