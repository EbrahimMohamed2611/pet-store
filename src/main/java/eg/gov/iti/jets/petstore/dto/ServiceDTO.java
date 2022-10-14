package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about the Service")
public class ServiceDTO {
    @Schema(description = "id for service")
    private Long id;
    @Schema(description = "Service name")
    private String name;
    @Schema(description = "Service price")
    private Float price;
    @Schema(description = "Service description")
    private String description;
    @Schema(description = "Service current discount")
    private Float discount;
    @Schema(description = "Starting time within work days to provide service")
    private LocalTime startTime;
    @Schema(description = "End time within work days to provide service")
    private LocalTime endTime;
    @Schema(description = "If Service is provided from home or not")
    private Boolean deliverable;
    @Schema(description = "Service image")
    private String imageUrl;
    @Schema(description = "Data of the service provider")
    private ServiceProviderDTO provider;
    @Schema(description = "Type of the service")
    private ServiceTypeDTO type;
    @Schema(description = "Service rates")
    private Set<ServiceRateDTO> rates = new HashSet<>(0);
}
