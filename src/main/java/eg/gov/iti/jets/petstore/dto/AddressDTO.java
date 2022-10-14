package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddressDTO {
    @Schema(description = "street of the address", example = "Nasr Street")
    private String street;
    @Schema(description = "city of the address", example = "Cairo")
    private String city;
    @Schema(description = "country of the address", example = "Egypt")
    private String country;
}
