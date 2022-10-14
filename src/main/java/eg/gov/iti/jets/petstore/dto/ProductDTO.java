package eg.gov.iti.jets.petstore.dto;

import eg.gov.iti.jets.petstore.entities.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about the Product")
public class ProductDTO {
    @Schema(description = "Product Id", example = "1")
    private Long id;
    @Schema(description = "Product Name", example = "CattyFood")
    private String name;
    @Schema(description = "Product Description", example = "Health food for your pets")
    private String description;
    @Schema(description = "Product Price", example = "19.99")
    private Float price;
    @Schema(description = "Product stock quantity", example = "26")
    private Integer quantity;
    @Schema(description = "Product category")
    private CategoryDTO category;
    @Schema(description = "Product Images")
    private Set<ProductImageDTO> images = new HashSet<>();
    @Schema(description = "Product availability", example = "true")
    private Boolean available ;
    @Schema(description = "Product Brand")
    private BrandDTO brand;
    @Schema(description = "Product species")
    private SpeciesDTO species;
    @Schema(description = "Product discount")
    private Float discount;
    @Schema(description = "Product Rates")
    private Set<RateDTO> rates = new HashSet<>();
}
