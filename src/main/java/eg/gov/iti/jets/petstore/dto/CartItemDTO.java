package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Details about the cart Items")
public class CartItemDTO {
    @Schema(description = "Details of product in shopping cart")
    private ProductDTO product;
    @Schema(description = "Quantity of product in shopping cart")
    private Integer quantity;

}
