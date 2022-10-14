package eg.gov.iti.jets.petstore.utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import eg.gov.iti.jets.petstore.exceptions.FileNotUploadedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class S3FileUploader implements FileUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3FileUploader.class);
    private static final String ERROR_MESSAGE = "File cannot be upload right now! try again later.";
    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3FileUploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket-name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Override
    public String upload(MultipartFile file, String... folders) {
        try {
            String foldersPath = Arrays.stream(folders).collect(Collectors.joining("/"));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            final String keyName = foldersPath + "/" + file.getOriginalFilename();
            amazonS3.putObject(bucketName, keyName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucketName, keyName).toString();
        } catch (IOException | AmazonClientException exception) {
            logError(exception);
            throw new FileNotUploadedException(ERROR_MESSAGE);
        }
    }

    private void logError(Exception exception) {
        if (exception instanceof AmazonServiceException) {
            AmazonServiceException amazonException = (AmazonServiceException) exception;
            LOGGER.error(" The call was transmitted successfully, but Amazon S3 couldn't process \n" +
                    "it, so it returned an error response., rejected reasons: ", amazonException);
            LOGGER.info("Error Message: {}", amazonException.getMessage());
            LOGGER.info("HTTP Status Code: {}", amazonException.getStatusCode());
            LOGGER.info("AWS Error Code: {}", amazonException.getErrorCode());
            LOGGER.info("Error Type: {}", amazonException.getErrorType());
            LOGGER.info("Request ID: {}", amazonException.getRequestId());
        } else {
            LOGGER.error("Caught {}: {}", exception.getClass().getName(), exception.getMessage());
        }
    }
}
