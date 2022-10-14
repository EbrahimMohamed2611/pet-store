package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Wrapper class for list of admins.")
public class AdminsDTO {
    @Schema(description = "List of admins accounts.")
    private List<AdminDTO> admins;
    @Schema(description = "Total number of admins accounts exist on the system", example = "2698750")
    private Long count;
}
