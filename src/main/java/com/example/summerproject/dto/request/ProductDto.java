package com.example.summerproject.dto.request;

import com.example.summerproject.entity.ProdType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    Long prodId;

    String name;

    Long stock;

    String prodType;

    Long selling_price;

    Long cost_price;

}