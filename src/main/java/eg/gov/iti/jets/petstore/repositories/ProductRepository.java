package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p set p.quantity =:newQuantity where p.id =:id")
    void updateProductQuantity(@Param("newQuantity") Integer newQuantity, @Param("id") Long id);

    Page<Product> findAllByPriceBetweenAndAvailableAndQuantityGreaterThan(Float minPrice, Float maxPrice, Boolean isAvailable, Integer quantity, Pageable pageable);

    Page<Product> findProductsByCategory_IdAndPriceBetweenAndAvailableAndQuantityGreaterThan(Long id, Float minPrice, Float maxPrice, Boolean isAvailable, Integer quantity, Pageable pageable);

    Page<Product> findProductsByBrand_IdAndPriceBetweenAndAvailableAndQuantityGreaterThan(Integer id, Float minPrice, Float maxPrice, Boolean isAvailable, Integer quantity, Pageable pageable);

    Page<Product> findProductsByCategory_IdAndBrand_IdAndPriceBetweenAndAvailableAndQuantityGreaterThan(Long categoryId, Integer brandId, Float minPrice, Float maxPrice, Boolean isAvailable, Integer quantity, Pageable pageable);

    @Query("SELECT p from Product p where p.id <= :s and p.quantity > 0 and p.available <> false order by(p.discount)")
    List<Product> getTheBestOfferForProducts(Long s);

    @Query("SELECT p from Product p where p.id <= :s and p.quantity > 0 and p.available <> false order by(p.creationDate)")
    List<Product> getTheTopRatedProductsProducts(Long s);

    @Query("select p from Product p inner join OrderItems o on o.product.id = p.id where o.product.quantity > 0 and o.product.available <> false group by o.product.id order by count(o.product.id) desc, sum(o.quantity) desc")
    Page<Product> getTheBestSellersProducts(Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainsAndAvailableAndQuantityGreaterThan(String name, Boolean isAvailable, Integer quantity, Pageable pageable);

}
