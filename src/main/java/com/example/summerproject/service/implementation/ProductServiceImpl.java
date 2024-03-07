package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.entity.Product;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.mapper.ProductMapper;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final CustomMessageSource messageSource;
    private final ProductMapper productMapper;

    @Override
    public String addProduct(ProductDto productDto) {

        Optional<Product> byName = productRepo.findByName(productDto.getName());
        if (byName.isPresent()) {
            Product existingproduct = byName.get();

            if (existingproduct.isDeleted()) {
                existingproduct.setDeleted(false);
                existingproduct.setStock(existingproduct.getStock() + productDto.getStock());
            } else {
                existingproduct.setStock(existingproduct.getStock() + productDto.getStock());
            }
            productRepo.save(existingproduct);
            return "Product "+productDto.getName()+" already existed so stock has been adjusted";
        } else {
            Product product = objectMapper.convertValue(productDto, Product.class);
            productRepo.save(product);
            return "product " + productDto.getName() + " added";
        }
    }

    @Override
    public String deleteProduct(Long prodId) {
        productRepo.findById(prodId)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        productRepo.deleteProd(prodId);
        return prodId + " has been deleted";
    }

    @Override
    public ProductResponseDto findProduct(String prodName) {
        return productMapper.getByName(prodName);
    }

    @Override
    public List<ProductResponseDto> findAllProduct() {
        return productMapper.getAllProducts();
    }
}
