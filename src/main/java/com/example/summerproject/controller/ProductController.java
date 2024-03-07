package com.example.summerproject.controller;


import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public GenericResponse<String> addAuthor(@RequestBody @Valid ProductDto productDto) {
        return successResponse(productService.addProduct(productDto), "Product added");
    }


    @Operation(summary = "Get all Product", description = "Get all products in the Store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-all-products")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<ProductResponseDto>> getAllProducts() {
        return successResponse(productService.findAllProduct(), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }


    @Operation(summary = "Get all Product", description = "Get all products in the Store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-products-By-Name")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<ProductResponseDto> getProductByName(String name) {
        return successResponse(productService.findProduct(name), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }
}
