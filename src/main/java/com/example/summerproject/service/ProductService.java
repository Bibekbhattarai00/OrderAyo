package com.example.summerproject.service;

import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.request.ProductFilterRequestDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    String addProduct(ProductDto productDto, MultipartFile file) throws IOException;
    String deleteProduct(Long prodId);
    public List<ProductResponseDto> findProduct();
    public Object findProductByType(String prodName);
    Object findAllProduct(ProductFilterRequestDto requestDto);
    List<ProductResponseDto> getAllProductsWithoutPaginationReq();
//    public String getImage(Long id, HttpServletResponse response) throws IOException ;
    public String getImageBase64(Long id) throws IOException ;

    public void getImage(Long id, HttpServletResponse response) throws IOException;

    public String exportToDb(MultipartFile file) throws IOException, IllegalAccessException, InstantiationException;
    public  String downloadExcel(HttpServletResponse response) throws IOException, IllegalAccessException;
}
