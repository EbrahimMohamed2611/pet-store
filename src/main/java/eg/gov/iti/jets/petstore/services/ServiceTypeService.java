package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.CategoryDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServiceTypeDTO;

import java.util.List;


public interface ServiceTypeService  {
    List<ServiceTypeDTO> getAllType();

    ServiceTypeDTO getTypeById(Integer id);

    ServiceTypeDTO addNewType(ServiceTypeDTO categoryDto);

    List<ServiceDTO> getTypeServices(Integer id);

}
