package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.ServiceDTO;
import eg.gov.iti.jets.petstore.dto.ServicesDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping(path = "services", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class ServiceResource {
    public final ServiceService serviceService;

    public ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Operation(summary = "find all service.",
            description = "Retrieve all service.")
    @ApiResponse(responseCode = "204", description = "Empty list of service", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve all service.")
    @GetMapping
    public ResponseEntity<ServicesDTO> getServices(@Parameter(description = "Minimum price of Services.", example = "0") @RequestParam(name = "price.lt", defaultValue = "0") Float minPrice,
                                                   @Parameter(description = "Maximum price of Services.", example = "10000") @RequestParam(name = "price.gt", defaultValue = "" + Float.MAX_VALUE) Float maxPrice,
                                                   @Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        ServicesDTO services = serviceService.getAllServices(minPrice, maxPrice, page, pageLimit);
        HttpStatus httpStatus = services.getServices().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(services);
    }

    @Operation(summary = "find specific service.",
            description = "Retrieve specific Service.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve service.")
    @ApiResponse(responseCode = "404", description = "Service not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceDTO getService(@Parameter(description = "Service unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        return serviceService.getService(id);
    }


    @Operation(summary = "Add new service .",
            description = "Insert new service.")
    @ApiResponse(responseCode = "201", description = "Successfully created service.")
    @ApiResponse(responseCode = "400", description = "Illegal representation of the service.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDTO addService(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Service information.", required = true) @RequestPart("service") ServiceDTO service,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Service image.", required = true) @RequestPart("image") MultipartFile image) {
        return serviceService.addService(service, image);
    }

    @Operation(summary = "Delete all service accounts.",
            description = "Delete all accounts with service privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete service accounts.")
    @ApiResponse(responseCode = "404", description = "Service accounts not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteServices() {
        serviceService.deleteAllServices();
    }

    @Operation(summary = "Update specific service.",
            description = "Update specific service.")
    @ApiResponse(responseCode = "200", description = "Successfully update service.")
    @ApiResponse(responseCode = "404", description = "Service not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PutMapping(path = "{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ServiceDTO updateService(@Parameter(description = "Service unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Service information.", required = true) @RequestPart("service") ServiceDTO service,
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Service image.") @RequestPart(value = "image", required = false) MultipartFile image) {
        return serviceService.updateService(id, service, Optional.ofNullable(image));
    }

    @Operation(summary = "Delete specific service.",
            description = "Delete specific service.")
    @ApiResponse(responseCode = "200", description = "Successfully delete service.")
    @ApiResponse(responseCode = "404", description = "Service not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteServices(@Parameter(description = "Service unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        serviceService.deleteService(id);
    }

    @Operation(summary = "find type's services.",
            description = "Retrieve all services belonging to specific type.")
    @ApiResponse(responseCode = "204", description = "Empty list of service", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve all service.")
    @GetMapping(params = {"typeId"})
    public ResponseEntity<ServicesDTO> getTypeServices(@Parameter(description = "Type unique identifier.", example = "1") @RequestParam(name = "typeId") Long typeId,
                                                       @Parameter(description = "Minimum price of services.", example = "0") @RequestParam(name = "price.lt", defaultValue = "0") Float minPrice,
                                                       @Parameter(description = "Maximum price of services.", example = "10000") @RequestParam(name = "price.gt", defaultValue = "" + Float.MAX_VALUE) Float maxPrice,
                                                       @Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        ServicesDTO services = serviceService.getServicesByTypeId(typeId, minPrice, maxPrice, page, pageLimit);
        HttpStatus httpStatus = services.getServices().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(services);
    }
}
