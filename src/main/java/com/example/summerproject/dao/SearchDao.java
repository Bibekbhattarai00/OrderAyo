package com.example.summerproject.dao;


import com.example.summerproject.dto.response.OrderResponseDto;
import com.example.summerproject.dto.response.ProdnameResponseDto;
import com.example.summerproject.dto.response.ProductResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.poi.util.LocaleID;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchDao {
    private final EntityManager entityManager;

    public List<OrderResponseDto> searchFilterDao( String customerName ,String customerContact ,String productName) {
        CriteriaBuilder cb=entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderResponseDto> result=cb.createQuery(OrderResponseDto.class);

        CriteriaQuery<ProductResponseDto> ProdResponseRoot=cb.createQuery(ProductResponseDto.class);
        Root<OrderResponseDto> OrderResponseRoot =result.from(OrderResponseDto.class);

        List<Predicate> fi=new ArrayList<>();

        if(customerName!=null){

        }

        return null;
    }


}