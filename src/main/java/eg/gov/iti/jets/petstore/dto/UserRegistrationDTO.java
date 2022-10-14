package eg.gov.iti.jets.petstore.dto;


import eg.gov.iti.jets.petstore.enums.Gender;
import eg.gov.iti.jets.petstore.enums.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about User Registration Data")
public class UserRegistrationDTO {

    @Schema(description = "User's First Name", example = "foo")
    private String firstName;
    @Schema(description = "User's last Name", example = "foo")
    private String lastName;
    @Schema(description = "User's email address", example = "foo@example.com")
    private String email;
    @Schema(description = "User's account hashed password", example = "!@ABc12345_#0")
    private String password;
    @Schema(description = "User's phone number", example = "01000100100")
    private String phoneNumber;
    @Schema(description = "User's address")
    private AddressDTO address;
    @Schema(description = "User's role", example = "ROLE_USER")
    private Roles role;
    @Schema(description = "User's account name", example = "foo")
    private String userName;
    @Schema(description = "User's gender", example = "MALE")
    private Gender gender;
    @Schema(description = "User's birth date", example = "1995-01-15")
    private LocalDate birthDate;

}
