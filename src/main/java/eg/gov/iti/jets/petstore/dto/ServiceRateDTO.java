package eg.gov.iti.jets.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about the Rate")
public class ServiceRateDTO {
    @Schema(description = "Customer identifier", example = "156", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long customerId;
    @Schema(description = "Service identifier", example = "20", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long serviceId;
    @Schema(description = "customer username", example = "Alex")
    @JsonProperty("userName")
    private String customerUserName;
    @Schema(description = "The rating number between 0 to 5", example = "5")
    private Integer rateNumber;
}
