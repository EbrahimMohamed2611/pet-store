package eg.gov.iti.jets.petstore.resources;


import eg.gov.iti.jets.petstore.dto.SpeciesDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.SpeciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/species")
@Tag(name = "Species", description = "Provide operations to deal with species.")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class SpeciesResource {
    private final SpeciesService speciesService;

    @Autowired
    public SpeciesResource(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @GetMapping(path = "/{speciesId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "find a species.",
            description = "Retrieve a species using its id.",
            responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve species."),
            @ApiResponse(responseCode = "400", description = "Bad request, you must provide id" , content = @Content),
            @ApiResponse(responseCode = "404", description = " No such Species id.",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
    public ResponseEntity<SpeciesDTO> getSpeciesById(@Parameter(in = ParameterIn.PATH, description = "Id of the species you need to retrieve ", required = true) @PathVariable Integer speciesId) {
        
        SpeciesDTO species = speciesService.getSpeciesById(speciesId);
        return ResponseEntity.status(HttpStatus.OK).body(species);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "find all species.",
            description = "Retrieve all species.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieve species."),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "204", description = "No species found", content = @Content)})
    public ResponseEntity<List<SpeciesDTO>> getAllSpecies() {
        List<SpeciesDTO> allSpecies = speciesService.getAllSpecies();
        return ResponseEntity.status(HttpStatus.OK).body(allSpecies);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new species.",
            description = "Add new species.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully added new species."),
                    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)})
    public ResponseEntity<SpeciesDTO> addNewSpecies(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Species information.", required = true)
                                                        @RequestBody SpeciesDTO speciesDTO) {
        SpeciesDTO newSpecies = speciesService.addNewSpecies(speciesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecies);
    }

    @Operation(summary = "Delete a species.",
            description = "Delete a species using its id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully delete species."),
                    @ApiResponse(responseCode = "400", description = "Bad request, you must provide id" , content = @Content),
                    @ApiResponse(responseCode = "404", description = " No such Species id.", content = @Content)})
    @DeleteMapping(path= "/{speciesId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteSpecies(@Parameter(in = ParameterIn.PATH, description = "Id of the species you need to delete ", required = true)
                                  @PathVariable("speciesId") Integer id){
        speciesService.deleteSpecies(id);
    }

   @PutMapping(path= "/{speciesId}", produces = MediaType.APPLICATION_JSON_VALUE)
   @Operation(summary = "Update a species.",
           description = "Edit information of a species. Must provide an id.",
           responses = {
                   @ApiResponse(responseCode = "200", description = "Successfully updated species."),
                   @ApiResponse(responseCode = "400", description = "Bad request, you must provide id and body" , content = @Content),
                   @ApiResponse(responseCode = "404", description = " No such Species id.", content = @Content)})
    public ResponseEntity<SpeciesDTO> updateSpecies( @Parameter(description = "Species unique identifier.", example = "2", required = true)
                                                        @PathVariable("speciesId")Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Species information.", required = true)
   @RequestBody SpeciesDTO speciesDTO){
       SpeciesDTO newSpecies = speciesService.updateSpecies(id, speciesDTO);
       return ResponseEntity.status(HttpStatus.OK).body(newSpecies);
   }
}
