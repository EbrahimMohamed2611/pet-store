package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductsDTO {
    @Schema(description = "List of Products")
    private List<ProductDTO> products;
    @Schema(description = "Total number of pages for products", example = "15")
    private Long count;
}
