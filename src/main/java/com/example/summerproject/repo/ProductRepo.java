package com.example.summerproject.repo;

import com.example.summerproject.entity.Product;
import com.example.summerproject.generic.GenericSoftDeleteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepo extends GenericSoftDeleteRepository<Product, Long> {
    Optional<Product> findByName(String ProdName);


    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_products SET deleted=true  WHERE prod_id=?1",nativeQuery = true)
    void deleteProd(Long id);

}
