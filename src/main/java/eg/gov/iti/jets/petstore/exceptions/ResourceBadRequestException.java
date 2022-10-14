package eg.gov.iti.jets.petstore.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceBadRequestException extends BaseException {
    public ResourceBadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
