package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Service;
import eg.gov.iti.jets.petstore.entities.ServiceProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    @Query(value = "select p.services from ServiceProvider p where p.id = :id",
            countQuery = "select count(s.id) from Service s where s.provider.id = :id")
    Page<Service> getProviderServices(@Param("id") Long id, Pageable pageable);
}
