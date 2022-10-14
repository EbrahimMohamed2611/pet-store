package eg.gov.iti.jets.petstore.services.impl;

import eg.gov.iti.jets.petstore.dto.CartItemDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.entities.CartItem;
import eg.gov.iti.jets.petstore.entities.Customer;
import eg.gov.iti.jets.petstore.entities.Product;
import eg.gov.iti.jets.petstore.exceptions.ResourceBadRequestException;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.CustomerRepository;
import eg.gov.iti.jets.petstore.repositories.ProductRepository;
import eg.gov.iti.jets.petstore.services.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingCartServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Set<CartItemDTO> updateProductFromShoppingCart(Long customerId, ProductDTO productDTO, Integer quantity) {

        if (quantity == 0)
            throw new ResourceBadRequestException(" 0  quantities isn't acceptable");

        //TO Handle bad request
        Product product = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productDTO.getId() + " not found"));
        int availableQuantity = product.getQuantity();
        //to check if quantity is available
        if (quantity > availableQuantity)
            throw new ResourceBadRequestException("A maximum of " + availableQuantity + " items is available from this product");


        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " not found"));
        Set<CartItem> shoppingCart = customer.getShoppingCart();
        Optional<CartItem> optionalItem = shoppingCart
                .stream()
                .filter(s -> s.getProduct().getId() == productDTO.getId())
                .findFirst();

        CartItem cartItem;
        if (optionalItem.isPresent()) {
            cartItem = optionalItem.get();
            if ((cartItem.getQuantity() + quantity) <= 0)
                throw new ResourceBadRequestException(" 0 or minus quantities aren't acceptable");
            //to check if quantity in cart and new quantity are less than available quantity
            if ((cartItem.getQuantity() + quantity) > availableQuantity)
                throw new ResourceNotFoundException("A maximum of " + availableQuantity + " items is available from this product");
            else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setProduct(modelMapper.map(productDTO, Product.class));
        }

        shoppingCart.add(cartItem);
        shoppingCart.stream().forEach(s -> s.setCustomer(customer));
        customer.setShoppingCart(shoppingCart);
        customerRepository.save(customer);
        Set<CartItemDTO> shoppingCartDTO = shoppingCart.stream().map(s -> modelMapper.map(s, CartItemDTO.class)).collect(Collectors.toSet());
        return shoppingCartDTO;
    }


    @Override
    public Set<CartItemDTO> removeProductFromShoppingCart(Long customerId, Long productId) {
        //To handle bad request
        if (productRepository.existsById(productId)) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " not found"));
            Set<CartItem> shoppingCart = customer.getShoppingCart();
            shoppingCart.stream()
                    .filter(s -> s.getProduct().getId() == productId)
                    .findFirst()
                    .ifPresent(item -> shoppingCart.remove(item));
            customer.setShoppingCart(shoppingCart);
            customerRepository.save(customer);
            return shoppingCart.stream()
                    .map(s -> modelMapper.map(s, CartItemDTO.class))
                    .collect(Collectors.toSet());
        } else {
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        }
    }

    @Override
    public Set<CartItemDTO> getShoppingCartByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " not found"));
        Set<CartItem> shoppingCart = customer.getShoppingCart();
        return shoppingCart.stream()
                .map(s -> modelMapper.map(s, CartItemDTO.class))
                .collect(Collectors.toSet());
    }

//    @Override
//    public Map<Long, Boolean> proceedToCheckout(Long customerId, Set<CartItemDTO> cartItemDTOS) {
//        Map<Long, Boolean> itemFromStock = new HashMap<>();
//        checkProductInStock(cartItemDTOS, itemFromStock);
////        updateProductQuantityAfterCheckout(cartItemDTOS, itemFromStock);
//        return itemFromStock;
//    }


    private void checkProductInStock(Set<CartItemDTO> cartItemDTOS, Map<Long, Boolean> itemFromStock) {
        cartItemDTOS.forEach(item -> {
            Integer productQuantityFromDatabase = productRepository.getById(item.getProduct().getId()).getQuantity();
            if (item.getQuantity() > productQuantityFromDatabase)
                itemFromStock.put(item.getProduct().getId(), true);
        });
    }

}
