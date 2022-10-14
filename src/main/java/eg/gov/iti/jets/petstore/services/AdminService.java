package eg.gov.iti.jets.petstore.services;

import eg.gov.iti.jets.petstore.dto.AdminDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import java.util.List;
import eg.gov.iti.jets.petstore.dto.AdminsDTO;


public interface AdminService {
    AdminsDTO getAllAdmins(Integer page, Integer pageLimit);

    AdminDTO getAdmin(Long id);

    AdminDTO addAdmin(AdminDTO admin);

    AdminDTO updateAdmin(Long id, AdminDTO admin);

    void deleteAdmin(Long id);

    void deleteAllAdmins();
    void signUp(UserRegistrationDTO userRegistrationDTO);

}
