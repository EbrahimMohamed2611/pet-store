package eg.gov.iti.jets.petstore.repositories;


import eg.gov.iti.jets.petstore.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findAllByPriceBetween(Float minPrice, Float maxPrice, Pageable pageable);
    Page<Service> findServicesByType_IdAndPriceBetween(Long id, Float minPrice, Float maxPrice, Pageable pageable);}
