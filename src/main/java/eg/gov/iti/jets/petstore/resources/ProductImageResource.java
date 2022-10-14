package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.ProductImageService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("images")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class ProductImageResource {

    @Autowired
    ProductImageService productImageService;

    @PostMapping("/upload")
    public String uploadSingleImage(@RequestParam("product") ProductDTO productDTO,
                                    @RequestParam("file") MultipartFile file) {
        String keyName = file.getOriginalFilename();
        productImageService.uploadSingleImage(productDTO, file);
        return "Upload Successfully -> KeyName = " + keyName;
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteSingleImage(@PathParam(value = "imageName") final String imageName,
                                                    @PathParam("productName") final String productName,
                                                    @PathParam("categoryName") final String categoryName) {

        boolean isSingleImageDeleted = productImageService.deleteSingleImage(imageName, productName, categoryName);
        final String imagePath = categoryName + '/' + productName + '/' + imageName;
        if (isSingleImageDeleted) {
            final String response = "[" + imagePath + "] deleted successfully.";
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/delete/{product_id}")
    public ResponseEntity<String> deleteImagesOfProduct(@PathVariable("product_id") long id) {

        boolean isSingleImageDeleted = productImageService.deleteImagesOfProduct(id);
        if (isSingleImageDeleted) {
            return new ResponseEntity<>("deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("failed to delete.", HttpStatus.NOT_FOUND);
        }

    }


}
