package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.*;

import java.util.List;

public interface ServiceProviderService {
    ServiceProvidersDTO getAllServiceProviders(Integer page, Integer pageLimit);

    ServiceProviderDTO getServiceProvider(Long id);

    ServiceProviderDTO addServiceProvider(ServiceProviderDTO serviceProvider);

    ServiceProviderDTO updateServiceProvider(Long id, ServiceProviderDTO serviceProvider);

    void deleteServiceProvider(Long id);

    void deleteAllServiceProviders();

    void signUp(UserRegistrationDTO userRegistrationDTO);

    ServicesDTO getProviderServices(Long id, Integer page, Integer pageLimit);

}
