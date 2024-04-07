package com.example.summerproject.mapper;

import com.example.summerproject.dto.response.ProductResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select tp.prod_id as prodId , tp.created_by as modifiedBy ,tp.\"name\" as prodName , tp.cost_price as CostPrice, \n" +
            "tp.selling_price as SellingPrice ,tp.prod_type as prod_type ,tp.stock as AvailableStock\n" +
            "from tbl_products tp where tp.deleted =false")
    List<ProductResponseDto> getAllProducts();

    @Select("select tp.prod_id as prodId , tp.created_by as modifiedBy ,tp.\"name\" as prodName , tp.cost_price as CostPrice, \n" +
            "tp.selling_price as SellingPrice ,tp.prod_type as prodType ,tp.stock as AvailableStock\n" +
            "from tbl_products tp where tp.deleted =false and tp.name =#{name}")
    ProductResponseDto getByName(String name);


}
