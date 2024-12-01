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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @GetMapping("/get-out-of-stock-products")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<ProductResponseDto>> getProductByName() {
        return successResponse(productService.findProduct(), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }


    @Operation(summary = "Remove Product", description = "Remove products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "dispatch Success"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @DeleteMapping("/dispatch-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public GenericResponse<String> deleteProduct(@RequestParam Long id) {
        return successResponse(productService.deleteProduct(id), "products has been removed");
    }

    @Operation(summary = "Get product image by id", description = "Fetch product image based on  provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-image-by-id")
    public GenericResponse<String> getPhoto(@RequestParam Long id, HttpServletResponse response) throws IOException {
        productService.getImage(id,response);
        return successResponse("Product", "Prod id-:" + id + " details");
    }

    @Operation(summary = "Get product image by id base 64", description = "Fetch product image based on  provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-image-by-id-base64")
    public GenericResponse<String> getPhotoBase64(@RequestParam Long id) throws IOException {
//        productService.getImage(id);
        return successResponse(productService.getImageBase64(id), "prod id-:" + id + " details");
    }


    @Operation(summary = "Upload product details", description = "upload product detail based of excel sheet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "product uploaded"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping(value = "/export-to-db" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String> exportToDb(@ModelAttribute MultipartFile file) throws IOException, IllegalAccessException, InstantiationException {
        return successResponse(productService.exportToDb(file),"data exported");
    }

    @Operation(summary = "Upload product details", description = "upload product detail based of excel sheet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "product uploaded"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping(value = "/import-to-excel")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String> importToExcel(HttpServletResponse response) throws IOException, IllegalAccessException, InstantiationException {
        return successResponse(productService.downloadExcel(response),"data imported to excel");
    }
}
