package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.ProductsDTO;
import eg.gov.iti.jets.petstore.dto.SellerDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.dto.SellersDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "sellers", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class SellerResource {
    public final SellerService sellerService;

    public SellerResource(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Operation(summary = "find all seller accounts.",
            description = "Retrieve all account with seller privilege.")
    @ApiResponse(responseCode = "204", description = "Empty list of seller accounts.", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve seller accounts.")
    @GetMapping
    public ResponseEntity<SellersDTO> getSellers(@Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                 @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        SellersDTO sellers = sellerService.getAllSellers(page, pageLimit);
        HttpStatus httpStatus = sellers.getCount() <= 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(sellers);
    }

    @Operation(summary = "find specific seller account.",
            description = "Retrieve specific account with seller privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve seller account.")
    @ApiResponse(responseCode = "404", description = "Seller account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SellerDTO getSeller(@Parameter(description = "Seller account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        return sellerService.getSeller(id);
    }

    @Operation(summary = "find specific seller products.",
            description = "Retrieve products related to specific seller account.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve seller products.")
    @ApiResponse(responseCode = "204", description = "Empty list of seller products.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Seller account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}/products")
    public ResponseEntity<ProductsDTO> getSellerProducts(@Parameter(description = "Seller account unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                                         @Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        ProductsDTO sellerProducts = sellerService.getSellerProducts(id, page, pageLimit);
        HttpStatus httpStatus = sellerProducts.getCount() == 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(sellerProducts);
    }

    @Operation(summary = "Add new seller account.",
            description = "Insert new account with seller privilege.")
    @ApiResponse(responseCode = "201", description = "Successfully created seller account.")
    @ApiResponse(responseCode = "400", description = "Illegal representation of the seller account.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SellerDTO addSeller(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller account information.", required = true) @RequestBody SellerDTO seller) {
        return sellerService.addSeller(seller);
    }

    @Operation(summary = "Delete all seller accounts.",
            description = "Delete all accounts with seller privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete seller accounts.")
    @ApiResponse(responseCode = "404", description = "Seller accounts not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteSellers() {
        sellerService.deleteAllSellers();
    }

    @Operation(summary = "Update specific seller account.",
            description = "Update specific account with seller privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully update seller account.")
    @ApiResponse(responseCode = "404", description = "Seller account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SellerDTO updateSeller(@Parameter(description = "Seller account unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller account information.", required = true) @RequestBody SellerDTO seller) {
        return sellerService.updateSeller(id, seller);
    }

    @Operation(summary = "Delete specific seller account.",
            description = "Delete specific account with seller privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete seller account.")
    @ApiResponse(responseCode = "404", description = "Seller account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSellers(@Parameter(description = "Seller account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        sellerService.deleteSeller(id);
    }
    @Operation(summary = "SignUp Seller",
            description = "provide User information to SignUp"
    )
    @ApiResponse(responseCode = "201", description = "Successfully SignUp.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)
    @PostMapping("signUp")
    public ResponseEntity<HttpStatus> signUp ( @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User Information ToSign Up", required = true) @RequestBody UserRegistrationDTO userRegistrationDTO){
        System.out.println("userRegistrationDTO " + userRegistrationDTO);
        sellerService.signUp(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
