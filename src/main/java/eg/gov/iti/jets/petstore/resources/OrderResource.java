package eg.gov.iti.jets.petstore.resources;


import eg.gov.iti.jets.petstore.dto.OrderDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.OrderService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/customers/{id}/orders")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class OrderResource {
    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }


//    @Operation(summary = "find All Order",
//            description = "Retrieve all orders for admin to see users history",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Successfully retrieve orders."),
//                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
//                    @ApiResponse(responseCode = "204", description = "No order found", content = @Content)})
//    @GetMapping
//    public ResponseEntity<List<OrderDTO>> getAllOrders( @Parameter(description = "Customer identifier.", example = "123", required = true) @PathVariable("id") Long customerId) {
//        List<OrderDTO> ordersForSpecificUser = orderService.getOrderForSpecificUser(customerId);
//        return ResponseEntity.status(HttpStatus.OK).body(ordersForSpecificUser);
//    }


//    @Operation(summary = "Finds Order by User id with order status not completed",
//            description = "Provide a user id to look up order with status not completed",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Successfully retrieve user order."),
//                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
//                    @ApiResponse(responseCode = "204", description = "No order found", content = @Content)})
//
//    @GetMapping(path = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<OrderDTO> getOrderWithStatusNotCompletedForUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User Id to get his order", required = true)@PathVariable Long userId){
//        OrderDTO orderForSpecificUser = orderService.getOrderForSpecificUser(userId);
//        Link link = linkTo(OrderResource.class).slash(userId).withSelfRel();
//        orderForSpecificUser.add(link);
//        return ResponseEntity.status(HttpStatus.OK).body(orderForSpecificUser);
//    }


    @Operation(summary = "Add new order.",
            description = "Insert new order.")
    @ApiResponse(responseCode = "500", description = "Interval Server Error.")
    @ApiResponse(responseCode = "201", description = "Successfully created Order.")
    @ApiResponse(responseCode = "400", description = "Illegal representation of the Order.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping
    public ResponseEntity<OrderDTO> createNewOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "order information.", required = true) @RequestBody OrderDTO orderDTO,
                                                   @Parameter(description = "Customer identifier.", example = "123", required = true) @PathVariable("id") Long customerId) {
        OrderDTO newOrder = orderService.createNewOrder(orderDTO, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    }
