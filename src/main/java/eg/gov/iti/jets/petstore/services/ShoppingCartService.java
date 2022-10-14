package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.CartItemDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.entities.CartItem;

import java.util.Map;
import java.util.Set;

public interface ShoppingCartService {

    Set<CartItemDTO> updateProductFromShoppingCart(Long customerId, ProductDTO productDTO, Integer quantity);
    Set<CartItemDTO> removeProductFromShoppingCart(Long customerId, Long productId);
    Set<CartItemDTO> getShoppingCartByCustomerId(Long customerId);
//    Map<Long, Boolean> proceedToCheckout(Long customerId, Set<CartItemDTO> cartItemDTOS);
}
