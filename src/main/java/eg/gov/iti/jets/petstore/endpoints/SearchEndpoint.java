package eg.gov.iti.jets.petstore.endpoints;

import eg.gov.iti.jets.petstore.dto.ProductsDTO;
import eg.gov.iti.jets.petstore.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchEndpoint {
    private final ProductService productService;

    public SearchEndpoint(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "search all product.",
            description = "search all product for matching.")
    @ApiResponse(responseCode = "204", description = "Empty list of product", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve all product.")
    @GetMapping(params = "q")
    public ResponseEntity<ProductsDTO> searchProducts(@Parameter(description = "Product name to query for.", example = "Catty") @RequestParam("q") String query,
                                                      @Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        ProductsDTO products = productService.searchProducts(query, page, pageLimit);
        HttpStatus status = products.getCount() <= 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(products);
    }
}
