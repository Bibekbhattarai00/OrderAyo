package com.example.summerproject.controller;


import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.request.ProductFilterRequestDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "APIs for managing Products")
public class ProductController extends BaseController {
    private final ProductService productService;
    private final CustomMessageSource messageSource;


    @Operation(summary = "Add Product", description = "Add products to the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/add-product")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String> addProduct(@ModelAttribute @Valid ProductDto productDto, MultipartFile file) throws IOException {
        return successResponse(productService.addProduct(productDto, file), "Product added");
    }


    @Operation(summary = "Get all Product", description = "Get all products in the Store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/get-all-products")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<Object> getAllProducts(@RequestBody ProductFilterRequestDto requestDto) {
        return successResponse(productService.findAllProduct(requestDto), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }

    @Operation(summary = "Get all Product", description = "Get all products in the Store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/all-products-without-pagination")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<ProductResponseDto>> getAllProducts() {
        return successResponse(productService.getAllProductsWithoutPaginationReq(), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }



    @Operation(summary = "Get all Product", description = "Get all products in the Store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-products-By-Name")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<ProductResponseDto> getProductByName(@RequestParam String name) {
        return successResponse(productService.findProduct(name), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }


    @Operation(summary = "Remove Product", description = "Remove products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "dispatch Success"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @DeleteMapping("/dispatch-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public GenericResponse<String>  deleteProduct(@RequestParam Long id){
        return successResponse(productService.deleteProduct(id),"products has been removed");
    }

    @Operation(summary = "Get book image by id", description = "Fetch Book image based on  provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-image-by-id")
    public GenericResponse<String> getPhoto(@RequestParam Long id, HttpServletResponse response) throws IOException {
        productService.getImage(id,response);
        return successResponse("Book", "book id-:" + id + " details");
    }

}
