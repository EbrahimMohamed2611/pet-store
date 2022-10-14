package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about the Product Images")
public class ProductImageDTO {

    @Schema(description = "Product Image Id")
    private Long id;
    @Schema(description = "Product Image name")
    private String name;
    @Schema(description = "Product Image url")
    private String url;
}
