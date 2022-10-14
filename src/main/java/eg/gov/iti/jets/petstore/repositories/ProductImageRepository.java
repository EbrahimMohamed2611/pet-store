package eg.gov.iti.jets.petstore.repositories;


import eg.gov.iti.jets.petstore.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{

    int deleteProductImageByUrl(String url);

    int deleteByProduct_Id(long id);

    @Query("select i from ProductImage i join Product p where p.id = ?1")
    List<ProductImage> findByProductId(long id);

    List<ProductImage> findByProduct_Name(String productName);
}
