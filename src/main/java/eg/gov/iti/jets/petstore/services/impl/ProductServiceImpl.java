package eg.gov.iti.jets.petstore.services.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.dto.ProductsDTO;
import eg.gov.iti.jets.petstore.entities.Product;
import eg.gov.iti.jets.petstore.entities.ProductImage;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.repositories.ProductRepository;
import eg.gov.iti.jets.petstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, AmazonS3 amazonS3) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.amazonS3 = amazonS3;
    }

    @Override
    public ProductsDTO getAllProducts(Float minPrice, Float maxPrice, Integer page, Integer pageLimit) {
        Page<Product> products = productRepository.findAllByPriceBetweenAndAvailableAndQuantityGreaterThan(minPrice, maxPrice, true, 0, Pageable.ofSize(pageLimit).withPage(page));
        return ProductsDTO.builder()
                .count(products.getTotalElements())
                .products(products.stream()
                        .map(e -> modelMapper.map(e, ProductDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ProductDTO getProduct(Long id) {
        return productRepository.findById(id)
                .map(e -> modelMapper.map(e, ProductDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found."));
    }

    @Override
    public ProductDTO addProduct(ProductDTO product) {
        return modelMapper.map(productRepository.save(modelMapper.map(product, Product.class)), ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        product.setId(id);
        return modelMapper.map(productRepository.save(modelMapper.map(product, Product.class)), ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Product with id: " + id + " not found.");
        }
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAllInBatch();
    }

    @Override
    public ProductsDTO getProductsByCategoryId(Long id, Float minPrice, Float maxPrice, Integer page, Integer pageLimit) {
        Page<Product> products = productRepository.findProductsByCategory_IdAndPriceBetweenAndAvailableAndQuantityGreaterThan(id, minPrice, maxPrice, true, 0, Pageable.ofSize(pageLimit).withPage(page));
        return buildProductsDTO(products.getTotalElements(), products.get());
    }

    @Override
    public ProductsDTO getProductsByBrandId(Integer id, Float minPrice, Float maxPrice, Integer page, Integer pageLimit) {
        Page<Product> products = productRepository.findProductsByBrand_IdAndPriceBetweenAndAvailableAndQuantityGreaterThan(id, minPrice, maxPrice, true, 0, Pageable.ofSize(pageLimit).withPage(page));
        return buildProductsDTO(products.getTotalElements(), products.get());
    }

    @Override
    public ProductsDTO getProductsByCategoryAndBrand(Long categoryId, Integer brandId, Float minPrice, Float maxPrice, Integer page, Integer pageLimit) {
        Page<Product> products = productRepository.findProductsByCategory_IdAndBrand_IdAndPriceBetweenAndAvailableAndQuantityGreaterThan(categoryId, brandId, minPrice, maxPrice, true, 0, Pageable.ofSize(pageLimit).withPage(page));
        return buildProductsDTO(products.getTotalElements(), products.get());
    }

    @Override
    public List<ProductDTO> getTheBestOfferForProducts(Long size) {
        return productRepository.getTheBestOfferForProducts(size).stream().map(p ->
                modelMapper.map(p, ProductDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getTopRatedProducts(Long size) {
        return productRepository.getTheTopRatedProductsProducts(size).stream().map(p ->
                modelMapper.map(p, ProductDTO.class)).collect(Collectors.toList());
    }

    private ProductsDTO buildProductsDTO(Long count, Stream<Product> products) {
        return ProductsDTO.builder()
                .count(count)
                .products(
                        products.map(e -> modelMapper.map(e, ProductDTO.class))
                                .collect(Collectors.toList())
                ).build();
    }

    @Override
    @Transactional
    public ProductDTO addProductWithImages(MultipartFile[] files, String productDTOJson) throws JsonProcessingException {
        Product product = new ObjectMapper().readValue(productDTOJson, Product.class);
        if (product.getImages() == null) {
            product.setImages(new HashSet<>());
        }
        if (files != null) {
            Arrays.stream(files).forEach(file -> {
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
                    product.getImages().add(image);

                } catch (IOException ioe) {
                    logger.error("IOException: {}", ioe.getMessage());
                } catch (AmazonServiceException ase) {
                    logAmazonServiceException(ase);
                } catch (AmazonClientException ace) {
                    logger.error("Caught an AmazonClientException: ", ace);
                    logger.info("Error Message: {}", ace.getMessage());
                }
            });
        }
        return new ModelMapper().map(productRepository.save(product), ProductDTO.class);
    }

    @Override
    public ProductsDTO searchProducts(String query, Integer page, Integer pageLimit) {
        Page<Product> products = productRepository.findProductsByNameIgnoreCaseContainsAndAvailableAndQuantityGreaterThan(query, true, 0, Pageable.ofSize(pageLimit).withPage(page));
        return ProductsDTO.builder()
                .count(products.getTotalElements())
                .products(products
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .getContent())
                .build();
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
