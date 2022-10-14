package eg.gov.iti.jets.petstore.services.impl;

import eg.gov.iti.jets.petstore.dto.OrderDTO;
import eg.gov.iti.jets.petstore.entities.CartItem;
import eg.gov.iti.jets.petstore.entities.Customer;
import eg.gov.iti.jets.petstore.entities.Order;
import eg.gov.iti.jets.petstore.entities.OrderItems;
import eg.gov.iti.jets.petstore.enums.OrderStatus;
import eg.gov.iti.jets.petstore.repositories.CustomerRepository;
import eg.gov.iti.jets.petstore.repositories.OrderRepository;
import eg.gov.iti.jets.petstore.repositories.ProductRepository;
import eg.gov.iti.jets.petstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<OrderDTO> getAllOrder() {
        List<Order> orderList = orderRepository.findAll();
        return orderList
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO createNewOrder(OrderDTO orderDTO, Long customerId) {
        Customer customer = customerRepository.getById(customerId);
        Set<CartItem> shoppingCart = customer.getShoppingCart();
        shoppingCart.stream().forEach(orderItemDTO -> {

            Integer productQuantityFromDatabase = productRepository.getById(orderItemDTO.getProduct().getId()).getQuantity();
            Integer newQuantity = productQuantityFromDatabase - orderItemDTO.getQuantity();
            System.out.println("update " + newQuantity);
            productRepository.updateProductQuantity(newQuantity, orderItemDTO.getProduct().getId());
        });
        Order order = modelMapper.map(orderDTO, Order.class);
        shoppingCart.stream().forEach(cartItem ->
                order.getItems().add(
                        new OrderItems(cartItem.getQuantity(), cartItem.getProduct().getDiscount() * cartItem.getProduct().getPrice(), cartItem.getProduct(), order)
                ));
        order.setCustomer(customer);
        order.setStatus(OrderStatus.COMPLETED);
        Order orderAfterSaved = orderRepository.save(order);
        customer.getShoppingCart().clear();
        customerRepository.save(customer);
        return modelMapper.map(orderAfterSaved, OrderDTO.class);
    }


    //this method is used to get the not completed Order( Shopping cart )

    @Override
    public List<OrderDTO> getOrderForSpecificUser(Long userId) {
        Set<Order> orderSet = orderRepository.getOrderByUserId(userId);
        return orderSet.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }


}
