package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.ServiceProviderDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.dto.ServiceProvidersDTO;
import eg.gov.iti.jets.petstore.dto.ServicesDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.ServiceProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "serviceproviders", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class ServiceProviderResource {
    public final ServiceProviderService serviceProviderService;

    public ServiceProviderResource(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @Operation(summary = "find all serviceProvider accounts.",
            description = "Retrieve all account with serviceProvider privilege.")
    @ApiResponse(responseCode = "204", description = "Empty list of serviceProvider accounts.", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve serviceProvider accounts.")
    @GetMapping
    public ResponseEntity<ServiceProvidersDTO> getServiceProviders(@Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                        @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        ServiceProvidersDTO serviceProviders = serviceProviderService.getAllServiceProviders(page, pageLimit);
        HttpStatus httpStatus = serviceProviders.getCount() <= 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(serviceProviders);
    }

    @Operation(summary = "find specific serviceProvider account.",
            description = "Retrieve specific account with serviceProvider privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve serviceProvider account.")
    @ApiResponse(responseCode = "404", description = "ServiceProvider account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceProviderDTO getServiceProvider(@Parameter(description = "ServiceProvider account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        return serviceProviderService.getServiceProvider(id);
    }

    @Operation(summary = "find specific serviceProvider Service.",
            description = "Retrieve Service related to specific serviceProvider account.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve serviceProvider Service.")
    @ApiResponse(responseCode = "204", description = "Empty list of serviceProvider's services.", content = @Content)
    @ApiResponse(responseCode = "404", description = "ServiceProvider account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}/services")
    public ResponseEntity<ServicesDTO> getServiceProviderServices(@Parameter(description = "ServiceProvider account unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                                                  @Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                  @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        ServicesDTO providerServices = serviceProviderService.getProviderServices(id, page, pageLimit);
        HttpStatus httpStatus = providerServices.getCount() <= 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(providerServices);
    }

    @Operation(summary = "Add new serviceProvider account.",
            description = "Insert new account with serviceProvider privilege.")
    @ApiResponse(responseCode = "201", description = "Successfully created serviceProvider account.")
    @ApiResponse(responseCode = "400", description = "Illegal representation of the serviceProvider account.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceProviderDTO addServiceProvider(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ServiceProvider account information.", required = true) @RequestBody ServiceProviderDTO serviceProvider) {
        return serviceProviderService.addServiceProvider(serviceProvider);
    }

    @Operation(summary = "Delete all serviceProvider accounts.",
            description = "Delete all accounts with serviceProvider privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete serviceProvider accounts.")
    @ApiResponse(responseCode = "404", description = "ServiceProvider accounts not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteServiceProviders() {
        serviceProviderService.deleteAllServiceProviders();
    }

    @Operation(summary = "Update specific serviceProvider account.",
            description = "Update specific account with serviceProvider privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully update serviceProvider account.")
    @ApiResponse(responseCode = "404", description = "ServiceProvider account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceProviderDTO updateServiceProvider(@Parameter(description = "ServiceProvider account unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ServiceProvider account information.", required = true) @RequestBody ServiceProviderDTO serviceProvider) {
        return serviceProviderService.updateServiceProvider(id, serviceProvider);
    }

    @Operation(summary = "Delete specific serviceProvider account.",
            description = "Delete specific account with serviceProvider privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete serviceProvider account.")
    @ApiResponse(responseCode = "404", description = "ServiceProvider account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteServiceProviders(@Parameter(description = "ServiceProvider account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        serviceProviderService.deleteServiceProvider(id);
    }

    @Operation(summary = "SignUp Service Provider",
            description = "provide User information to SignUp"
    )
    @ApiResponse(responseCode = "201", description = "Successfully SignUp.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)
    @PostMapping("signUp")
    public ResponseEntity<HttpStatus> signUp ( @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User Information ToSign Up", required = true) @RequestBody UserRegistrationDTO userRegistrationDTO){
        System.out.println("userRegistrationDTO " + userRegistrationDTO);
        serviceProviderService.signUp(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
