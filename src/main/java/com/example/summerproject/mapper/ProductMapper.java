package com.example.summerproject.mapper;

import com.example.summerproject.dto.response.ProductResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select(" SELECT tp.prod_id AS prodId, \n" +
            "       tp.created_by AS modifiedBy, \n" +
            "       tp.\"name\" AS prodName, \n" +
            "       tp.cost_price AS costPrice, \n" +
            "       tp.selling_price AS sellingPrice, \n" +
            "       tp.prod_type AS prodType, \n" +
            "       tp.stock AS availableStock\n" +
            "FROM tbl_products tp \n" +
            "WHERE tp.stock <=0\n")
    List<ProductResponseDto> getAllOutOfStockProducts();

    @Select("select tp.prod_id as prodId , tp.created_by as modifiedBy ,tp.\"name\" as prodName , tp.cost_price as CostPrice, \n" +
            "tp.selling_price as SellingPrice ,tp.prod_type as prodType ,tp.stock as AvailableStock\n" +
            "from tbl_products tp where tp.deleted =false and tp.name =#{name}")
    ProductResponseDto getByName(String name);


    @Select(value = "SELECT tp.prod_id AS prodId, \n" +
            "       tp.created_by AS modifiedBy, \n" +
            "       tp.name AS prodName, \n" +
            "       tp.cost_price AS costPrice, \n" +
            "       tp.selling_price AS sellingPrice, \n" +
            "       tp.prod_type AS prodType, \n" +
            "       tp.stock AS availableStock\n" +
            "FROM tbl_products tp \n" +
            "WHERE tp.deleted = false ")
    List<ProductResponseDto> getAllProductsWithoutPaginationReq();

}
