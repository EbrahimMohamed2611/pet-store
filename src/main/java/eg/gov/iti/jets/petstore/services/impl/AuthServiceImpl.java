package eg.gov.iti.jets.petstore.services.impl;

import eg.gov.iti.jets.petstore.dto.UserLoginDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.entities.Customer;
import eg.gov.iti.jets.petstore.entities.User;
import eg.gov.iti.jets.petstore.enums.Roles;
import eg.gov.iti.jets.petstore.repositories.*;
import eg.gov.iti.jets.petstore.security.model.CustomUserDetails;
import eg.gov.iti.jets.petstore.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${secret.password}")
    private String password;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder ) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @Override
    public UserRegistrationDTO signUp(UserRegistrationDTO userRegistrationDTO) {
        userRegistrationDTO.setPassword(bCryptPasswordEncoder.encode(userRegistrationDTO.getPassword()));
        User user = modelMapper.map(userRegistrationDTO, User.class);
        user.setActive(true);
        user.setNotLocked(true);
        User save = userRepository.save(user);
        return modelMapper.map(save, UserRegistrationDTO.class);
    }

    @Override
    public String signIn(UserLoginDTO userLoginDTO) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        return new CustomUserDetails(user);
    }
    @Override
    public Boolean isUserEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public CustomUserDetails addNewUser(String email) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(bCryptPasswordEncoder.encode(password));
        customer.setRole(Roles.ROLE_CUSTOMER);
        customer.setActive(true);
        customer.setNotLocked(true);

        User newUser = userRepository.save(customer);
        return new CustomUserDetails(newUser);
    }
}
