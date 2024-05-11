package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.request.ProductFilterRequestDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.entity.Product;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.generic.pagination.CustomPagination;
import com.example.summerproject.mapper.ProductMapper;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final CustomMessageSource messageSource;
    private final ProductMapper productMapper;
    private final CustomPagination customPagination;


    @Override
    public String addProduct(ProductDto productDto,MultipartFile file) throws IOException {

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
            String path = saveImage("/uploads/", file);
            product.setImage(path);
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
    public ProductResponseDto findProductByType(String prodName) {
        return productMapper.getByName(prodName);
    }

    @Override
    public Object findAllProduct(ProductFilterRequestDto requestDto) {
//        return productMapper.getAllProducts(requestDto.getName(), requestDto.getProdType(),requestDto.getPageable());
        return customPagination.getPaginatedData(
                productRepo.getAllProducts(requestDto.getName().trim(),requestDto.getProdType(),requestDto.getPageable()));
    }

    @Override
    public List<ProductResponseDto> getAllProductsWithoutPaginationReq() {
        return productMapper.getAllProductsWithoutPaginationReq();
    }

    public static String saveImage(String path, MultipartFile file) throws IOException {
        if (file == null) {
            throw new NotFoundException("photo is req");
        }
        String name = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String filePath = path + File.separator + name;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return name;
    }


    @Override
    public void getImage(Long id, HttpServletResponse response) throws IOException {
        Product product = productRepo.findById(id).orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        String name = product.getImage();
//        InputStream stream = new FileInputStream("C:\\Users\\shyam prasad\\Pictures\\Saved Pictures\\" + name);
        InputStream stream = new FileInputStream("/uploads/" + name);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("image/jpeg");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=book_" + id + ".jpg";
        response.setHeader(headerKey, headerValue);
        byte[] imageByte = IOUtils.toByteArray(stream);
        out.write(imageByte);
        out.flush();
        out.close();
    }
}
