package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Wrapper class for list of service providers.")
public class ServiceProvidersDTO {
    @Schema(description = "List of service providers.")
    private List<ServiceProviderDTO> providers;
    @Schema(description = "Total number of service providers accounts exist on the system", example = "2698750")
    private Long count;
}
