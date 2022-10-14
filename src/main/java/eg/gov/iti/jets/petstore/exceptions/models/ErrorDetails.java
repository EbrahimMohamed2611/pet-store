package eg.gov.iti.jets.petstore.exceptions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorDetails {
    @NonNull
    @Schema(description = "The code of the error.", example = "404")
    private String code;
    @Schema(description = "The time of the error occurred.")
    private LocalDateTime date = LocalDateTime.now();
    @NonNull
    @Schema(description = "The URI that error occurred within.", example = "https://www.foo.com/api/users")
    private String uri;
    @NonNull
    @Schema(description = "The message related to error with is more human readable.", example = "User not found with specified id: 123.")
    private String message;

}
