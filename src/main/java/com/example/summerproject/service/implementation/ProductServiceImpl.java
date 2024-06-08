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
import com.example.summerproject.utils.ExcelGenerator;
import com.example.summerproject.utils.ExcelToDb;
import com.example.summerproject.utils.FilePathConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.example.summerproject.utils.NullValues.getNullPropertyNames;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final CustomMessageSource messageSource;
    private final ProductMapper productMapper;
    private final CustomPagination customPagination;


    @Override
    public String addProduct(ProductDto productDto, MultipartFile file) throws IOException {
        if (productDto.getProdId() != null) {
            Product product = productRepo.findById(productDto.getProdId())
                    .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
            BeanUtils.copyProperties(productDto, product, getNullPropertyNames(productDto));
            String path = saveImage("/uploads/", file);
//            String path = saveImage("C:/Users/shyam prasad/Pictures/products image/", file);
            product.setImage(path);
            productRepo.save(product);
            return "products has been updated";
        }
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
            return "Product " + productDto.getName() + " already existed so stock has been adjusted";
        } else {
            Product product = objectMapper.convertValue(productDto, Product.class);
            String path = saveImage("/uploads/", file);
//            String path = saveImage("C:\\Users\\shyam prasad\\Pictures\\products image", file);
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
    public List<ProductResponseDto> findProduct() {
        return productMapper.getAllOutOfStockProducts();
    }

    @Override
    public Object findProductByType(String prodName) {
        return productRepo.findAll();
    }

    @Override
    public Object findAllProduct(ProductFilterRequestDto requestDto) {
//        return productMapper.getAllProducts(requestDto.getName(), requestDto.getProdType(),requestDto.getPageable());
        return customPagination.getPaginatedData(
                productRepo.getAllProducts(requestDto.getName().trim(), requestDto.getProdType(), requestDto.getPageable()));
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
        InputStream stream = new FileInputStream("/uploads/" + name);
//        InputStream stream = new FileInputStream("C:/Users/shyam prasad/Pictures/products image/" + name);
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

    @Override
    public String getImageBase64(Long id) throws IOException {
        Product product = productRepo.findById(id).orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        String name = product.getImage();
//        InputStream stream = new FileInputStream("C:/Users/shyam prasad/Pictures/products image/"+ name);
        InputStream stream = new FileInputStream("/uploads/" + name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Read image content into ByteArrayOutputStream
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        byte[] imageBytes = baos.toByteArray();

        // Encode byte array to Base64
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);

        return base64EncodedImage;
    }

    @Override
    public String exportToDb(MultipartFile file) throws IOException, IllegalAccessException, InstantiationException {
        List<Product> products = ExcelToDb.createExcel(file, Product.class);
        productRepo.saveAll(products);
        return messageSource.get(ExceptionMessages.EXPORT_EXCEL_SUCCESS.getCode());
    }

    public String downloadExcel(HttpServletResponse response) throws IOException, IllegalAccessException {
        ExcelGenerator.generateExcel(response, productRepo.findInactive(), "product excel sheet", Product.class);
        return messageSource.get(ExceptionMessages.DOWNLOADED.getCode());
    }
}
