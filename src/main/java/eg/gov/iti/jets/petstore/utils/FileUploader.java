package eg.gov.iti.jets.petstore.utils;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    String upload(MultipartFile file, String... folders);
}
