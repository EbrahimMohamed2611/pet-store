package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.CategoryDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategory();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO addNewCategory(CategoryDTO categoryDto);

    List<ProductDTO> getCategoryProducts(Long id);

    List<CategoryDTO> findTheTopCategories(Long categoryId);
}
