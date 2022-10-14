package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.UserLoginDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.security.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    UserRegistrationDTO signUp(UserRegistrationDTO userRegistrationDTO);
    String signIn(UserLoginDTO userLoginDTO);
    Boolean isUserEmailExist(String email);
    CustomUserDetails addNewUser(String email);
}
