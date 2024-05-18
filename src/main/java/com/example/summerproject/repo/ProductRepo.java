package com.example.summerproject.repo;

import com.example.summerproject.dto.response.ProductResponseDto;
import com.example.summerproject.entity.Product;
import com.example.summerproject.generic.repo.GenericSoftDeleteRepository;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepo extends GenericSoftDeleteRepository<Product, Long> {
    Optional<Product> findByName(String ProdName);


    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_products SET deleted=true  WHERE prod_id=?1",nativeQuery = true)
    void deleteProd(Long id);




    @Query(value = "SELECT tp.prod_id AS prodId, \n" +
            "       tp.created_by AS modifiedBy, \n" +
            "       tp.\"name\" AS prodName, \n" +
            "       tp.cost_price AS costPrice, \n" +
            "       tp.selling_price AS sellingPrice, \n" +
            "       tp.prod_type AS prodType, \n" +
            "       tp.deleted as deleted ," +
            "       tp.stock AS availableStock\n" +
            "FROM tbl_products tp \n" +
            "WHERE (tp.prod_type = COALESCE(?2, tp.prod_type))\n" +
            "AND (\n" +
            "    CASE \n" +
            "        WHEN ?1 = '-1' THEN TRUE\n" +
            "        ELSE (tp.\"name\" LIKE CONCAT('%', ?1, '%'))\n" +
            "    END\n" +
            ")\n" , nativeQuery = true)
    Page<Map<String, Object>> getAllProducts(String name, String type , Pageable pageable);

}
