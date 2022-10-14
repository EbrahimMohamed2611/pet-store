package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.CartItemDTO;
import eg.gov.iti.jets.petstore.dto.OrderDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {

    List<OrderDTO> getAllOrder();
    OrderDTO createNewOrder(OrderDTO orderDTO, Long customerId);
    List<OrderDTO> getOrderForSpecificUser(Long userId);

}
