package eg.gov.iti.jets.petstore.services.impl;



import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServiceTypeDTO;
import eg.gov.iti.jets.petstore.entities.ServiceType;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.ServiceTypeRepository;
import eg.gov.iti.jets.petstore.services.ServiceTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;
    private final ModelMapper modelMapper;
    
    @Autowired
    public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository, ModelMapper modelMapper) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ServiceTypeDTO> getAllType() {
        List<ServiceType> typeList = serviceTypeRepository.findAll();
        return typeList
                .stream()
                .map(type -> modelMapper.map(type, ServiceTypeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ServiceTypeDTO getTypeById(Integer id) {
        Optional<ServiceType> typeOptional = serviceTypeRepository.findById(id);
        if (typeOptional.isPresent()) {
            ServiceType type = typeOptional.get();
            return modelMapper.map(type, ServiceTypeDTO.class);
        } else {
            throw new ResourceNotFoundException("Type with id: " + id + " not found");
        }
    }

    @Override
    public ServiceTypeDTO addNewType(ServiceTypeDTO typeDto) {
        ServiceType type = modelMapper.map(typeDto, ServiceType.class);
        ServiceType typeAfterSaved = serviceTypeRepository.save(type);
        return modelMapper.map(typeAfterSaved, ServiceTypeDTO.class);
    }

    @Override
    public List<ServiceDTO> getTypeServices(Integer id) {
        return serviceTypeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("type with id: " + id + " not found."))
                .getServices()
                .stream()
                .map(e->modelMapper.map(e, ServiceDTO.class))
                .collect(Collectors.toList());
    }
}
