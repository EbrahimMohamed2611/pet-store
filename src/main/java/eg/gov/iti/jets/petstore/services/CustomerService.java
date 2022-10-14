package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.CustomerDTO;
import eg.gov.iti.jets.petstore.dto.CustomersDTO;
import eg.gov.iti.jets.petstore.dto.OrderDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;

import java.util.List;

public interface CustomerService {
    CustomersDTO getAllCustomers(Integer page, Integer pageLimit);

    CustomerDTO getCustomer(Long id);

    CustomerDTO addCustomer(CustomerDTO customer);

    CustomerDTO updateCustomer(Long id, CustomerDTO customer);

    void deleteCustomer(Long id);

    void deleteAllCustomers();

    List<OrderDTO> getCustomerOrders(Long id);
    void signUp(UserRegistrationDTO userRegistrationDTO);


}
