package eg.gov.iti.jets.petstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Details about species")
public class SpeciesDTO {

    @Schema(description = "The species id", example = "2")
    private Integer speciesId;
    @Schema(description = "The species name", example = "Cat")
    private String speciesName;
}
