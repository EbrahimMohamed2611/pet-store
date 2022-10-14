package eg.gov.iti.jets.petstore.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.dto.ProductsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductsDTO getAllProducts(Float minPrice, Float maxPrice, Integer page, Integer pageLimit);

    ProductDTO getProduct(Long id);

    ProductDTO addProduct(ProductDTO product);

    ProductDTO updateProduct(Long id, ProductDTO product);

    void deleteProduct(Long id);

    void deleteAllProducts();

    ProductsDTO getProductsByCategoryId(Long id, Float minPrice, Float maxPrice, Integer page, Integer pageLimit);

    ProductsDTO getProductsByBrandId(Integer id, Float minPrice, Float maxPrice, Integer page, Integer pageLimit);

    ProductsDTO getProductsByCategoryAndBrand(Long categoryId, Integer brandId, Float minPrice, Float maxPrice, Integer page, Integer pageLimit);

    /**
     * <h2>Add Product with Multiple Images</h2>
     * This method is a candidate for asynchronous execution.
     * It accepts an array of images represented as an
     * array of MultiPartfile and a json string structured as
     * ProductDTO.
     * The AddNum program implements an application that
     * simply adds two given integer numbers and Prints
     * the output on the screen.
     * <p>
     * <b>Note:</b> Giving proper property names in json whivh match
     * the name of the fields in ProdcutDTO class or the object mapepr
     * won't map the json to object of ProductDTO properly.
     * user friendly and it is assumed as a high quality code.
     * @param files : array of MultiPartFile.
     * @param productDTOJson : json represent productDTO
     * @return new ProductDTO containing the uploaded images.
     * @author  Yahya Allam
     * @version 1.0
     * @since   2021-06-19
     */
    ProductDTO addProductWithImages(MultipartFile[] files, String productDTOJson) throws JsonProcessingException;
  
    List<ProductDTO> getTheBestOfferForProducts(Long size);
  
    List<ProductDTO> getTopRatedProducts(Long size);

    ProductsDTO searchProducts(String query, Integer page, Integer pageLimit);
}
