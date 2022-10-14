package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.BrandDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/brands", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class BrandResource {

    private final BrandService brandService;

    @Autowired
    public BrandResource(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Finds Brand by id",
            description = "Provide and id to look up specific brand"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve brand.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "brand not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping(path = "/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrandDTO> getBrandById(@Parameter(in = ParameterIn.PATH, example = "123", description = "Id value for the brand you need to retrieve ", required = true) @PathVariable Integer brandId) {
        BrandDTO brand = brandService.getBrandById(brandId);
        return ResponseEntity.status(HttpStatus.OK).body(brand);
    }


    @Operation(summary = "Get all brands",
            description = "Retrieve all available brands"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve all brands.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "brands not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllCategories() {
        List<BrandDTO> allBrand = brandService.getAllBrand();
        return ResponseEntity.status(HttpStatus.OK).body(allBrand);
    }


    @Operation(summary = "Add new brand", description = "Add new brand for exists brands")
    @ApiResponse(responseCode = "200", description = "Successfully added new brand.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping
    public ResponseEntity<BrandDTO> addNewBrand(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "brand information.", required = true)
                                                @RequestBody BrandDTO brandDto) {
        BrandDTO newBrand = brandService.addNewBrand(brandDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
    }

    @Operation(summary = "find specific brand products.",
            description = "Retrieve products related to specific brand.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve Brand products.")
    @ApiResponse(responseCode = "204", description = "Empty list of Brand products.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Brand not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{brandId}/products")
    public ResponseEntity<List<ProductDTO>> getBrandProducts(@Parameter(description = "Brand unique identifier.", example = "123", required = true) @PathVariable("brandId") Integer brandId) {
        List<ProductDTO> products = brandService.getBrandProducts(brandId);
        HttpStatus status = products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(products);
    }

    @Operation(summary = "Delete all brands.",
            description = "Delete all brands exist on the system.")
    @ApiResponse(responseCode = "200", description = "Successfully delete brands data.")
    @ApiResponse(responseCode = "404", description = "Brand not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteSellers() {
        brandService.deleteAllBrands();
    }

    @Operation(summary = "Delete specific brand.",
            description = "Delete specific brand data.")
    @ApiResponse(responseCode = "200", description = "Successfully delete brand data.")
    @ApiResponse(responseCode = "404", description = "Brand not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping("{brandId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSellers(@Parameter(description = "Brand unique identifier.", example = "123", required = true) @PathVariable("brandId") Integer brandId) {
        brandService.deleteBrand(brandId);
    }
}
