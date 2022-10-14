package eg.gov.iti.jets.petstore.services.impl;


import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServicesDTO;
import eg.gov.iti.jets.petstore.entities.Service;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.ServiceRepository;
import eg.gov.iti.jets.petstore.services.ServiceService;
import eg.gov.iti.jets.petstore.utils.FileUploader;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final FileUploader fileUploader;


    public ServiceServiceImpl(ServiceRepository serviceRepository, ModelMapper modelMapper, FileUploader fileUploader) {
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
        this.fileUploader = fileUploader;
    }


    @Override
    public ServicesDTO getAllServices(Float minPrice, Float maxPrice, Integer page, Integer pageLimit) {
        Page<Service> services = serviceRepository.findAllByPriceBetween(minPrice, maxPrice, Pageable.ofSize(pageLimit).withPage(page));
        return ServicesDTO.builder()
                .count(services.getTotalElements())
                .services(services.stream()
                        .map(e -> modelMapper.map(e, ServiceDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ServiceDTO getService(Long id) {
        return serviceRepository.findById(id)
                .map(e -> modelMapper.map(e, ServiceDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Service with id: " + id + " not found."));
    }

    @Override
    public ServiceDTO addService(ServiceDTO serviceDTO, MultipartFile image) {
        serviceDTO.setImageUrl(fileUploader.upload(image, serviceDTO.getType().getName(), serviceDTO.getName()));
        return modelMapper
                .map(serviceRepository.save(modelMapper.map(serviceDTO, eg.gov.iti.jets.petstore.entities.Service.class)), ServiceDTO.class);
    }

    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO, Optional<MultipartFile> image) {
        if (serviceRepository.existsById(id)) {
            serviceDTO.setId(id);
            image.map(img -> fileUploader.upload(img, serviceDTO.getType().getName(), serviceDTO.getName()))
                    .ifPresent(serviceDTO::setImageUrl);
            return modelMapper
                    .map(serviceRepository.save(modelMapper.map(serviceDTO, eg.gov.iti.jets.petstore.entities.Service.class)), ServiceDTO.class);
        } else {
            throw new ResourceNotFoundException("Service with id: " + id + " not found.");
        }
    }

    @Override
    public void deleteService(Long id) {
        try {
            serviceRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Service with id: " + id + " not found.");
        }
    }

    private ServicesDTO buildServicesDTO(Long count, Stream<Service> services) {
        return ServicesDTO.builder()
                .count(count)
                .services(
                        services.map(e -> modelMapper.map(e, ServiceDTO.class))
                                .collect(Collectors.toList())
                ).build();
    }

    @Override
    public void deleteAllServices() {
        serviceRepository.deleteAll();
    }

    @Override
    public ServicesDTO getServicesByTypeId(Long id, Float minPrice, Float maxPrice, Integer page, Integer pageLimit) {
        Page<Service> services = serviceRepository.findServicesByType_IdAndPriceBetween(id, minPrice, maxPrice, Pageable.ofSize(pageLimit).withPage(page));
        return buildServicesDTO(services.getTotalElements(), services.get());
    }
}
