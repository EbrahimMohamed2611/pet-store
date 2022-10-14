package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.AdminDTO;
import eg.gov.iti.jets.petstore.dto.UserRegistrationDTO;
import eg.gov.iti.jets.petstore.dto.AdminsDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "admins", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class AdminResource {
    public final AdminService adminService;

    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "find all admin accounts.",
            description = "Retrieve all account with admin privilege.")
    @ApiResponse(responseCode = "204", description = "Empty list of admin accounts.", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve admin accounts.")
    @GetMapping
    public ResponseEntity<AdminsDTO> getAdmins(@Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                               @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        AdminsDTO admins = adminService.getAllAdmins(page, pageLimit);
        HttpStatus httpStatus = admins.getCount() <= 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(admins);
    }

    @Operation(summary = "find specific admin account.",
            description = "Retrieve specific account with admin privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve admin account.")
    @ApiResponse(responseCode = "404", description = "Admin account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdminDTO getAdmin(@Parameter(description = "Admin account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        return adminService.getAdmin(id);
    }

    @Operation(summary = "Add new admin account.",
            description = "Insert new account with administrator privilege.")
    @ApiResponse(responseCode = "201", description = "Successfully created admin account.")
    @ApiResponse(responseCode = "400", description = "Illegal representation of the admin account.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDTO addAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Admin account information.", required = true) @RequestBody AdminDTO admin) {
        return adminService.addAdmin(admin);
    }

    @Operation(summary = "Delete all admin accounts.",
            description = "Delete all accounts with admin privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete admin accounts.")
    @ApiResponse(responseCode = "404", description = "Admin accounts not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdmins() {
        adminService.deleteAllAdmins();
    }

    @Operation(summary = "Update specific admin account.",
            description = "Update specific account with admin privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully update admin account.")
    @ApiResponse(responseCode = "404", description = "Admin account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdminDTO updateAdmin(@Parameter(description = "Admin account unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Admin account information.", required = true) @RequestBody AdminDTO admin) {
        return adminService.updateAdmin(id, admin);
    }

    @Operation(summary = "Delete specific admin account.",
            description = "Delete specific account with admin privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete admin account.")
    @ApiResponse(responseCode = "404", description = "Admin account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdmins(@Parameter(description = "Admin account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        adminService.deleteAdmin(id);
    }

    @Operation(summary = "SignUp Admin",
            description = "provide User information to SignUp"
    )
    @ApiResponse(responseCode = "201", description = "Successfully SignUp.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)
    @PostMapping("signUp")
    public ResponseEntity<HttpStatus> signUp ( @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User Information ToSign Up", required = true) @RequestBody UserRegistrationDTO userRegistrationDTO){
        adminService.signUp(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
