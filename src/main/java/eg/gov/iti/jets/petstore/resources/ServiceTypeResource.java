package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServiceTypeDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.ServiceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class ServiceTypeResource {
    private final ServiceTypeService serviceTypeService;

    @Autowired
    public ServiceTypeResource(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }


    @Operation(summary = "Finds type by id",
            description = "Provide and id to look up specific type"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve type.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "type not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping(path = "/{typeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceTypeDTO> getTypeById(@Parameter(in = ParameterIn.PATH, example = "123", description = "Id value for the type you need to retrieve ", required = true) @PathVariable Integer typeId) {
        ServiceTypeDTO type = serviceTypeService.getTypeById(typeId);
        Link link = linkTo(ServiceTypeResource.class).slash(typeId).withSelfRel();
        Link typesLink = linkTo(ServiceTypeResource.class)
                .withRel("allTypes");
        type.add(link,typesLink);
        return ResponseEntity.status(HttpStatus.OK).body(type);
    }


    @Operation(summary = "Get all types",
            description = "Retrieve all available types"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve all types.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "types not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceTypeDTO>> getAllTypes() {
        List<ServiceTypeDTO> allType = serviceTypeService.getAllType();
        return ResponseEntity.status(HttpStatus.OK).body(allType);
    }


    @Operation(summary = "Add new type", description = "Add new tyoe for exists types")
    @ApiResponse(responseCode = "200", description = "Successfully added new type.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceTypeDTO> addNewType(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "type information.", required = true)
                                                      @RequestBody ServiceTypeDTO typeDTO) {
        ServiceTypeDTO newType = serviceTypeService.addNewType(typeDTO);

        Link link = linkTo(ServiceTypeResource.class).slash(newType.getId()).withSelfRel();
        Link typesLink = linkTo(ServiceTypeResource.class)
                .withRel("allTypes");
        newType.add(link, typesLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(newType);
    }

    @Operation(summary = "find specific type services.",
            description = "Retrieve services related to specific type.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve Type services.")
    @ApiResponse(responseCode = "204", description = "Empty list of Type services.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Type not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}/services")
    public ResponseEntity<List<ServiceDTO>> getTypeServices(@Parameter(description = "Type unique identifier.", example = "123", required = true) @PathVariable("id") Integer id) {
        List<ServiceDTO> services = serviceTypeService.getTypeServices(id);
        HttpStatus status = services.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(services);
    }
}
