package eg.gov.iti.jets.petstore.services.impl;


import eg.gov.iti.jets.petstore.dto.CustomerDTO;
import eg.gov.iti.jets.petstore.dto.CustomersDTO;
import eg.gov.iti.jets.petstore.dto.OrderDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.entities.Customer;
import eg.gov.iti.jets.petstore.entities.Seller;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.CustomerRepository;
import eg.gov.iti.jets.petstore.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public CustomersDTO getAllCustomers(Integer page, Integer pageLimit) {
        Page<Customer> customers = customerRepository.findAll(Pageable.ofSize(pageLimit).withPage(page));
        return CustomersDTO.builder()
                .count(customers.getTotalElements())
                .customers(customers
                        .map(e -> modelMapper.map(e, CustomerDTO.class))
                        .getContent())
                .build();
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(e -> modelMapper.map(e, CustomerDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found."));
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customer) {
        return modelMapper.map(customerRepository.save(modelMapper.map(customer, Customer.class)), CustomerDTO.class);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customer) {
        customer.setId(id);
        return modelMapper.map(customerRepository.save(modelMapper.map(customer, Customer.class)), CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Customer with id: " + id + " not found.");
        }
    }

    @Override
    public void deleteAllCustomers() {
        customerRepository.deleteAllInBatch();
    }

    @Override
    public List<OrderDTO> getCustomerOrders(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found."))
                .getOrders()
                .stream()
                .map(e -> modelMapper.map(e, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void signUp(UserRegistrationDTO userRegistrationDTO) {
        userRegistrationDTO.setPassword(bCryptPasswordEncoder.encode(userRegistrationDTO.getPassword()));
        Customer customer = modelMapper.map(userRegistrationDTO, Customer.class);
        customer.setActive(true);
        customer.setNotLocked(true);
        customerRepository.save(customer);
    }


}
