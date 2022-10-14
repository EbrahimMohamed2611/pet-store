package eg.gov.iti.jets.petstore.services.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.entities.Product;
import eg.gov.iti.jets.petstore.entities.ProductImage;
import eg.gov.iti.jets.petstore.repositories.ProductImageRepository;
import eg.gov.iti.jets.petstore.services.ProductImageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ModelMapper modelMapper;


    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private Logger logger = LoggerFactory.getLogger(ProductImageServiceImpl.class);


    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ModelMapper modelMapper, AmazonS3 amazonS3) {
        this.productImageRepository = productImageRepository;
        this.modelMapper = modelMapper;
        this.amazonS3 = amazonS3;
    }

    // @Async annotation ensures that the method is executed in a different background thread
    // but not consume the main thread.
    @Async
    @Override
    public void uploadSingleImage(ProductDTO productDTO, MultipartFile file) {
        Product product = modelMapper.map(productDTO, Product.class);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            final String keyName = product.getCategory().getName() + '/' +
                    product.getName() + '/' +
                    file.getOriginalFilename();
            amazonS3.putObject(bucketName, keyName, file.getInputStream(), metadata);
            logger.info("Url Object: {}", amazonS3.getUrl(bucketName, keyName));

            ProductImage image = ProductImage.builder()
                    .name(file.getOriginalFilename())
                    .url(amazonS3.getUrl(bucketName, keyName).toString())
                    .product(product)
                    .build();

            productImageRepository.save(image);

        } catch (IOException ioe) {
            logger.error("IOException: {}", ioe.getMessage());
        } catch (AmazonServiceException ase) {
            logAmazonServiceException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException: ", ace);
            logger.info("Error Message: {}", ace.getMessage());
        }

    }

    @Transactional
    @Override
    public Boolean deleteSingleImage(String imageName, String productName, String categoryName) {
        try {
            final String imagePath = categoryName + '/' + productName + '/' + imageName;
            //delete from S3 Bucket
            final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, imagePath);
            amazonS3.deleteObject(deleteObjectRequest);

            final String imageUrl = (amazonS3.getUrl(bucketName, imagePath).toString());

            //delete file info from database
            return productImageRepository.deleteProductImageByUrl(imageUrl) > 0;
        } catch (AmazonServiceException ase) {
            logAmazonServiceException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException: ", ace);
            logger.info("Error Message: {}", ace.getMessage());
        } catch (Exception e) {
            logger.error("Caught an Exception: ", e);
            logger.info("Error Message: {}", e.getMessage());
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteImagesOfProduct(long id) {
        List<ProductImage> imagesToBeDeleted = productImageRepository.findByProductId(id);
        List<String> imageKeys = new ArrayList<>();

        imagesToBeDeleted.forEach(image ->
                imageKeys.add(image.getProduct().getCategory().getName() + '/' +
                        image.getProduct().getName() + '/' +
                        image.getName()));

        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(String.valueOf(imageKeys));

            DeleteObjectsResult deleteObjectsResult = amazonS3.deleteObjects(deleteObjectsRequest);
            logger.info("Number of deleted Object in S3 bucket is {}", deleteObjectsResult.getDeletedObjects().size());
            productImageRepository.deleteByProduct_Id(id);
        } catch (AmazonServiceException ase) {
            logAmazonServiceException(ase);
            return false;
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException: ", ace);
            logger.info("Error Message: {}", ace.getMessage());
            return false;
        }
        return true;
    }

    private void logAmazonServiceException(AmazonServiceException ase) {
        logger.error(" The call was transmitted successfully, but Amazon S3 couldn't process \n" +
                "it, so it returned an error response., rejected reasons: ", ase);

        logger.info("Error Message: {}", ase.getMessage());
        logger.info("HTTP Status Code: {}", ase.getStatusCode());
        logger.info("AWS Error Code: {}", ase.getErrorCode());
        logger.info("Error Type: {}", ase.getErrorType());
        logger.info("Request ID: {}", ase.getRequestId());
    }
}
