package eg.gov.iti.jets.petstore.services.impl;

import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServiceProviderDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.dto.ServiceProvidersDTO;
import eg.gov.iti.jets.petstore.dto.ServicesDTO;
import eg.gov.iti.jets.petstore.entities.ServiceProvider;
import eg.gov.iti.jets.petstore.exceptions.ResourceBadRequestException;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.ServiceProviderRepository;
import eg.gov.iti.jets.petstore.services.ServiceProviderService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {
    private final ServiceProviderRepository serviceProviderRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ServiceProviderServiceImpl(ServiceProviderRepository serviceProviderRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public ServiceProvidersDTO getAllServiceProviders(Integer page, Integer pageLimit) {
        Page<ServiceProvider> providers = serviceProviderRepository.findAll(Pageable.ofSize(pageLimit).withPage(page));
        return ServiceProvidersDTO.builder()
                .count(providers.getTotalElements())
                .providers(providers
                        .map(e -> modelMapper.map(e, ServiceProviderDTO.class))
                        .getContent())
                .build();
    }

    @Override
    public ServiceProviderDTO getServiceProvider(Long id) {
        return serviceProviderRepository.findById(id)
                .map(e -> modelMapper.map(e, ServiceProviderDTO.class))
                .orElseThrow(() -> new ResourceBadRequestException("ServiceProvider with id: " + id + " not found"));
    }

    @Override
    public ServiceProviderDTO addServiceProvider(ServiceProviderDTO serviceProvider) {
        return modelMapper.map(serviceProviderRepository.save(modelMapper.map(serviceProvider, ServiceProvider.class)), ServiceProviderDTO.class);
    }

    @Override
    public ServiceProviderDTO updateServiceProvider(Long id, ServiceProviderDTO serviceProvider) {
        if (serviceProviderRepository.existsById(id)) {
            serviceProvider.setId(id);
            return modelMapper.map(serviceProviderRepository.save(modelMapper.map(serviceProvider, ServiceProvider.class)), ServiceProviderDTO.class);
        } else {
            throw new ResourceNotFoundException("ServiceProvider with id: " + id + " not found.");
        }
    }

    @Override
    public void deleteServiceProvider(Long id) {
        try {
            serviceProviderRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("ServiceProvider with id: " + id + " not found.");
        }

    }

    @Override
    public void deleteAllServiceProviders() {
        serviceProviderRepository.deleteAllInBatch();
    }

    @Override
    public ServicesDTO getProviderServices(Long id, Integer page, Integer pageLimit) {
        if (serviceProviderRepository.existsById(id)) {
            Page<eg.gov.iti.jets.petstore.entities.Service> providerServices = serviceProviderRepository.getProviderServices(id, Pageable.ofSize(pageLimit).withPage(page));
            return ServicesDTO.builder()
                    .count(providerServices.getTotalElements())
                    .services(providerServices
                            .map(service -> modelMapper.map(service, ServiceDTO.class))
                            .getContent())
                    .build();
        } else {
            throw new ResourceNotFoundException("ServiceProvider with id: " + id + " not found.");
        }
    }

    @Override
    public void signUp(UserRegistrationDTO userRegistrationDTO) {
        userRegistrationDTO.setPassword(bCryptPasswordEncoder.encode(userRegistrationDTO.getPassword()));
        ServiceProvider serviceProvider = modelMapper.map(userRegistrationDTO, ServiceProvider.class);
        serviceProvider.setActive(true);
        serviceProvider.setNotLocked(true);
        serviceProviderRepository.save(serviceProvider);
    }
}
