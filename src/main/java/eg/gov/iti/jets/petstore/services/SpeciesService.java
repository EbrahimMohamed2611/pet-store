package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.SpeciesDTO;

import java.util.List;

public interface SpeciesService {
    List<SpeciesDTO> getAllSpecies();
    SpeciesDTO getSpeciesByName(String speciesName);
    SpeciesDTO getSpeciesById(Integer speciesId);
    SpeciesDTO addNewSpecies(SpeciesDTO speciesDTO);
    SpeciesDTO updateSpecies(Integer id, SpeciesDTO speciesDTO);
    Boolean deleteSpecies(String speciesName);
    void deleteSpecies(Integer speciesId);
}
