package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User's Login Data")
public class UserLoginDTO  {

    @Schema(description = "User's email address", example = "foo@example.com")
    private String email;
    @Schema(description = "User's account hashed password", example = "!@ABc12345_#0")
    private String password;
}
