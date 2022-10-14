package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService{

    void uploadSingleImage(ProductDTO productDTO, MultipartFile file);
    Boolean deleteSingleImage(String imageName, String productName, String categoryName);
    Boolean deleteImagesOfProduct(long id);
}
