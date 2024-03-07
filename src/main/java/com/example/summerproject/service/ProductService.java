package com.example.summerproject.service;

import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.entity.Product;

import java.util.List;

public interface ProductService {
    String addProduct(ProductDto productDto);
    String deleteProduct(Long prodId);
    ProductResponseDto findProduct(String prodName);
    List<ProductResponseDto> findAllProduct();
}
