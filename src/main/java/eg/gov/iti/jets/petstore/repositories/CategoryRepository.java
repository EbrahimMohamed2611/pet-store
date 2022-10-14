package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.id <= :categorySize")
    List<Category> findTheTopCategories(@Param("categorySize") Long categorySize);
}
