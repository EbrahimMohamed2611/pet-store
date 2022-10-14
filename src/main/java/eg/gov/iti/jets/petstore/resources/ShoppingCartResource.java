package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.CartItemDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/customers/{id}/shoppingCart/")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class ShoppingCartResource {

    private final ShoppingCartService shoppingCartService;


    public ShoppingCartResource(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


    @Operation(summary = "update quantity of the product in cart .",
            description = "This method is used to add/update the quantity of the product in shopping cart.")
    @ApiResponse(responseCode = "400", description = "Bad request when not valid values sent.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @ApiResponse(responseCode = "404", description = "Number of products required exceeds available.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @ApiResponse(responseCode = "200", description = "Successfully modification done to shopping cart.")
    @PutMapping
    public ResponseEntity<Set<CartItemDTO>> updateShoppingCart(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product info to be used for updating.", required = true) @RequestBody ProductDTO product,
                                                            @Parameter(description = "Customer identifier.", example = "123", required = true) @PathVariable("id") Long customerId,
                                                            @Parameter(description = "Number of products.", example = "2") @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
        Set<CartItemDTO> updatedShoppingCart = shoppingCartService.updateProductFromShoppingCart(customerId, product, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(updatedShoppingCart);
    }

    @Operation(summary = "remove product from cart .",
            description = "This method is used to delete the product from shopping cart.")
    @ApiResponse(responseCode = "404", description = "No product/Customer with id is available.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @ApiResponse(responseCode = "200", description = "Successfully deletion done from shopping cart.")
    @DeleteMapping
    public ResponseEntity<Set<CartItemDTO>> removeProductFromShoppingCart(
            @Parameter(description = "Customer identifier.", example = "123", required = true) @PathVariable("id") Long customerId,
            @Parameter(description = "Product identifier", example = "2") @RequestParam(name = "productId") Long productId) {
        Set<CartItemDTO> updatedShoppingCart = shoppingCartService.removeProductFromShoppingCart(customerId, productId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedShoppingCart);
    }

    @Operation(summary = "Retrieve Customer's shopping Cart.",
            description = "To Get Customer's Shopping Cart.")
    @ApiResponse(responseCode = "404", description = "No Customer with id is available.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @ApiResponse(responseCode = "200", description = "Successfully deletion done from shopping cart.")
    @GetMapping
    public ResponseEntity<Set<CartItemDTO>> getShoppingCartById(
            @Parameter(description = "Customer identifier.", example = "123", required = true) @PathVariable("id") Long customerId) {
        Set<CartItemDTO> shoppingCart = shoppingCartService.getShoppingCartByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCart);
    }


//    @Operation(summary = "Check shopping cart validity",
//            description = "This method Check every cart item in stock.")
//    @ApiResponse(responseCode = "400", description = "Bad request when not valid values sent.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
//    @ApiResponse(responseCode = "404", description = "Number of products required exceeds available.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
//    @ApiResponse(responseCode = "200", description = "Successfully processing order to checkout.")
//    @PostMapping
//    public ResponseEntity<Map<Long,Boolean>> checkShoppingCartValidity(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart items details for checkout", required = true) @RequestBody Set<CartItemDTO> cartItemDTOS,
//                                                            @Parameter(description = "Customer identifier.", example = "123", required = true) @PathVariable("id") Long customerId) {
//        Map<Long,Boolean> ProductInStock = shoppingCartService.proceedToCheckout(customerId, cartItemDTOS);
//        return ResponseEntity.status(HttpStatus.OK).body(ProductInStock);
//    }

}
