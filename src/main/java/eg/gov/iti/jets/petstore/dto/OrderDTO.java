package eg.gov.iti.jets.petstore.dto;

import eg.gov.iti.jets.petstore.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about the Order")
public class OrderDTO extends RepresentationModel<OrderDTO> {
    @Schema(description = "id for order and it's unique for every order")
    private Long id;
    @Schema(description = "Order Date")
    private LocalDateTime date = LocalDateTime.now();
    @Schema(description = "The address to which the order will be shipped")
    private AddressDTO address;
    @Schema(description = "Status to represent Order Status", example = "NOT_COMPLETED")
    private OrderStatus status;
    @Schema(description = "Order Items for any Order")
    private Set<OrderItemDTO> items = new HashSet<>();
    @Schema(description = "Customer notes")
    private String customerNotes;

}
