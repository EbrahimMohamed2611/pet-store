package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Product;
import eg.gov.iti.jets.petstore.entities.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query(value = "select s.products from Seller s where s.id = :id",
            countQuery = "select count(p.id) from Product p where p.seller.id = :id")
    Page<Product> getSellerProducts(@Param("id") Long id, Pageable pageable);
}
