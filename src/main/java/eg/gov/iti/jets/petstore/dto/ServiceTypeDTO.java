package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Details about the ServiceTye")
public class ServiceTypeDTO extends RepresentationModel<ServiceTypeDTO> {
    @Schema(description = "The unique id of the Servicetype")
    private int id;
    @Schema(description = "The Servicetype name")
    private String name;
}
