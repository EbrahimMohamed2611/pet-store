package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Wrapper class for list of sellers.")
public class SellersDTO {
    @Schema(description = "List of sellers accounts.")
    private List<SellerDTO> sellers;
    @Schema(description = "Total number of sellers accounts exist on the system", example = "2698750")
    private Long count;
}
