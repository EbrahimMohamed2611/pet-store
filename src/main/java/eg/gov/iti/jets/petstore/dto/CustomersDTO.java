package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Wrapper class for list of customers.")
public class CustomersDTO {
    @Schema(description = "List of customers.")
    private List<CustomerDTO> customers;
    @Schema(description = "Total number of customers exist on the system", example = "2698750")
    private Long count;
}
