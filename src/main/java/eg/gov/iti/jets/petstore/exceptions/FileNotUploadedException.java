package eg.gov.iti.jets.petstore.exceptions;

import org.springframework.http.HttpStatus;

public class FileNotUploadedException extends BaseException {
    public FileNotUploadedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
