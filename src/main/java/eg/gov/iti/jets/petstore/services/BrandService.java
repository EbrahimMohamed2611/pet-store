package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.BrandDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;

import java.util.List;

public interface BrandService {
    List<BrandDTO> getAllBrand();

    BrandDTO getBrandById(Integer id);

    BrandDTO addNewBrand(BrandDTO brandDto);

    List<ProductDTO> getBrandProducts(Integer id);

    void deleteAllBrands();

    void deleteBrand(Integer id);
}
