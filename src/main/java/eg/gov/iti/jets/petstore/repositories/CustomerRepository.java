package eg.gov.iti.jets.petstore.repositories;


import eg.gov.iti.jets.petstore.entities.Customer;
import eg.gov.iti.jets.petstore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface CustomerRepository extends JpaRepository<Customer, Long> {



    //TODO Check if works
    Set<CartItem> findShoppingCartById(Long id);
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM CartItem where CartItem.cartItemId.customerId=:customerId")
//    Integer deleteShoppingCartForUser(@Param("customerId") Long customerId);
//
//    Integer del

}
