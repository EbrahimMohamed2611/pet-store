package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.*;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.CustomerService;
import eg.gov.iti.jets.petstore.services.EmailService;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class CustomerResource {
    public final CustomerService customerService;
    private final EmailService service;


    public CustomerResource(CustomerService customerService, EmailService service) {
        this.customerService = customerService;
        this.service = service;
    }

    @Operation(summary = "find all customer accounts.",
            description = "Retrieve all account with customer privilege.")
    @ApiResponse(responseCode = "204", description = "Empty list of customer accounts.", content = @Content)
    @ApiResponse(responseCode = "200", description = "Successfully retrieve customer accounts.")
    @GetMapping
    public ResponseEntity<CustomersDTO> getCustomers(@Parameter(description = "Number of pages to retrieve.", example = "0") @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @Parameter(description = "Number of accounts in the page.", example = "10") @RequestParam(name = "pageLimit", defaultValue = "10") Integer pageLimit) {
        CustomersDTO customers = customerService.getAllCustomers(page, pageLimit);
        HttpStatus httpStatus = customers.getCount() <= 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(customers);
    }

    @Operation(summary = "find specific customer account.",
            description = "Retrieve specific account with customer privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve customer account.")
    @ApiResponse(responseCode = "404", description = "customer account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomer(@Parameter(description = "customer account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }

    @Operation(summary = "find specific customer orders.",
            description = "Retrieve orders related to specific Customer account.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve Customer orders.")
    @ApiResponse(responseCode = "204", description = "Empty list of Customer orders.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Customer account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}/orders")
    public ResponseEntity<List<OrderDTO>> getCustomerOrder(@Parameter(description = "Customer account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        List<OrderDTO> orders = customerService.getCustomerOrders(id);
        HttpStatus status = orders.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(orders);
    }

    @Operation(summary = "Add new customer account.",
            description = "Insert new account with customer privilege.")
    @ApiResponse(responseCode = "201", description = "Successfully created customer account.")
    @ApiResponse(responseCode = "400", description = "Illegal representation of the customer account.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addCustomer(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer account information.", required = true) @RequestBody CustomerDTO customer) {
        return customerService.addCustomer(customer);
    }

    @Operation(summary = "Delete all customer accounts.",
            description = "Delete all accounts with customer privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete customer accounts.")
    @ApiResponse(responseCode = "404", description = "customer accounts not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomers() {
        customerService.deleteAllCustomers();
    }

    @Operation(summary = "Update specific customer account.",
            description = "Update specific account with customer privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully update customer account.")
    @ApiResponse(responseCode = "404", description = "customer account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomer(@Parameter(description = "customer account unique identifier.", example = "123", required = true) @PathVariable("id") Long id,
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "customer account information.", required = true) @RequestBody CustomerDTO customer) {
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Delete specific customer account.",
            description = "Delete specific account with customer privilege.")
    @ApiResponse(responseCode = "200", description = "Successfully delete customer account.")
    @ApiResponse(responseCode = "404", description = "customer account not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@Parameter(description = "customer account unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }


    @Operation(summary = "SignUp Customer",
            description = "provide User information to SignUp"
    )
    @ApiResponse(responseCode = "201", description = "Successfully SignUp.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)
    @PostMapping("signUp")
    public ResponseEntity<HttpStatus> signUp ( @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User Information ToSign Up", required = true) @RequestBody UserRegistrationDTO userRegistrationDTO){
        System.out.println("userRegistrationDTO " + userRegistrationDTO);
        customerService.signUp(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Contact US",
            description = "to send feedback to us"
    )
    @ApiResponse(responseCode = "201", description = "Successfully sent.")
    @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)

    @PostMapping("contact-us")
    public ResponseEntity<HttpStatus> contactUs(@RequestBody ContactUsDTO contactUsDTO) throws MessagingException, TemplateException, IOException {
        System.out.println("contactUsDTO Controller " + contactUsDTO);
        service.contactUs(contactUsDTO);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).build();
    }
}
