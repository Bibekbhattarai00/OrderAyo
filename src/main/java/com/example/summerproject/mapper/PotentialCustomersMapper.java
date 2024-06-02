package com.example.summerproject.mapper;

import com.example.summerproject.dto.response.PotentialCustomerResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PotentialCustomersMapper {

    @Select("select tbc.name as customerName,\n" +
            "           tbc.id as id,\n" +
            "           tp.name as productName,\n" +
            "           tbc.customer_email as email,\n" +
            "           tbc.phone as phone\n" +
            "    from tbl_potential_customers tbc\n" +
            "    join tbl_products tp on tbc.prod_id = tp.prod_id\n" +
            "    where tbc.deleted=false")
    List<PotentialCustomerResponseDto> getAllPotentialCustomer();
}
