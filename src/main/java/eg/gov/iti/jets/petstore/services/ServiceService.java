package eg.gov.iti.jets.petstore.services;


import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServicesDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface ServiceService {

    ServicesDTO getAllServices(Float minPrice, Float maxPrice, Integer page, Integer pageLimit);

    ServiceDTO getService(Long id);

    ServiceDTO addService(ServiceDTO serviceDTO, MultipartFile image);

    ServiceDTO updateService(Long id, ServiceDTO serviceDTO, Optional<MultipartFile> image);

    void deleteService(Long id);

    void deleteAllServices();

    ServicesDTO getServicesByTypeId(Long id, Float minPrice, Float maxPrice, Integer page, Integer pageLimit);

}
