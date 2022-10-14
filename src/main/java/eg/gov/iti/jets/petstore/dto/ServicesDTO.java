package eg.gov.iti.jets.petstore.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServicesDTO {
    @Schema(description = "List of Services")
    private List<ServiceDTO> services;
    @Schema(description = "Total number of pages for services", example = "15")
    private Long count;
}
