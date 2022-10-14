package eg.gov.iti.jets.petstore.services.impl;

import eg.gov.iti.jets.petstore.dto.BrandDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.entities.Brand;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.BrandRepository;
import eg.gov.iti.jets.petstore.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BrandDTO> getAllBrand() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BrandDTO getBrandById(Integer id) {
        return brandRepository.findById(id)
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id: " + id + " not found."));
    }

    @Override
    @Transactional
    public BrandDTO addNewBrand(BrandDTO brandDto) {
        return modelMapper.map(
                brandRepository.save(modelMapper.map(brandDto, Brand.class)),
                BrandDTO.class);
    }

    @Override
    public List<ProductDTO> getBrandProducts(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand with id: " + id + " not found."));
        return brand.getProducts().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllBrands() {
        brandRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteBrand(Integer id) {
        try {
            brandRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Brand with id: " + id + " not found.");
        }
    }
}
