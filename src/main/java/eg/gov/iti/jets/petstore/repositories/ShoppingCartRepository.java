package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//TODO Delete if not used
@Repository
public interface ShoppingCartRepository extends JpaRepository<CartItem,Long> {

//    @Override
//
//    Optional<CartItem> findById(Long aLong);

//    @Query("SELECT CartItem FROM ")
}
