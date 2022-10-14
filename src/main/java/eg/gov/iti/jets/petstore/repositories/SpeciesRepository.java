package eg.gov.iti.jets.petstore.repositories;

import eg.gov.iti.jets.petstore.entities.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpeciesRepository extends JpaRepository<Species, Integer> {
    Optional<Species> findSpeciesByName(String speciesName);
    long deleteSpeciesByName(String speciesName);
}
