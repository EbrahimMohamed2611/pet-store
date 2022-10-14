package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.CategoryDTO;
import eg.gov.iti.jets.petstore.dto.ProductDTO;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class CategoryResource {

    private final CategoryService categoryService;

    @Autowired
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Finds Category by id",
            description = "Provide and id to look up specific category"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve category.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "category not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping(path = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> getCategoryById(@Parameter(in = ParameterIn.PATH, example = "123", description = "Id value for the category you need to retrieve ", required = true) @PathVariable Long categoryId) {
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        Link link = linkTo(CategoryResource.class).slash(categoryId).withSelfRel();
        Link categoriesLink = linkTo(CategoryResource.class)
                .withRel("allCategories");
        category.add(link,categoriesLink);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }


    @Operation(summary = "Get all categories",
            description = "Retrieve all available categories"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve all categories.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "categories not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> allCategory = categoryService.getAllCategory();
        return ResponseEntity.status(HttpStatus.OK).body(allCategory);
    }


    @Operation(summary = "Get The first 3 Categories",
            description = "Retrieve The first 3 available categories"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieve The first 3 categories.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "categories not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping(params ={"size"})
    public ResponseEntity<List<CategoryDTO>> getTopThreeCategories(@Parameter(description = "Category size.", example = "3") @RequestParam(name = "size") Long size) {
        List<CategoryDTO> allCategory = categoryService.findTheTopCategories(size);
        return ResponseEntity.status(HttpStatus.OK).body(allCategory);
    }


    @Operation(summary = "Add new category", description = "Add new category for exists categories")
    @ApiResponse(responseCode = "200", description = "Successfully added new category.")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> addNewCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "category information.", required = true)
                                                          @RequestBody CategoryDTO categoryDto) {
        CategoryDTO newCategory = categoryService.addNewCategory(categoryDto);

        Link link = linkTo(CategoryResource.class).slash(newCategory.getId()).withSelfRel();
        Link categoriesLink = linkTo(CategoryResource.class)
                              .withRel("allCategories");
        newCategory.add(link, categoriesLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @Operation(summary = "find specific category products.",
            description = "Retrieve products related to specific category.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve Category products.")
    @ApiResponse(responseCode = "204", description = "Empty list of Category products.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Category not found.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    @GetMapping("{id}/products")
    public ResponseEntity<List<ProductDTO>> getCategoryProducts(@Parameter(description = "Category unique identifier.", example = "123", required = true) @PathVariable("id") Long id) {
        List<ProductDTO> products = categoryService.getCategoryProducts(id);
        HttpStatus status = products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(products);
    }
}
