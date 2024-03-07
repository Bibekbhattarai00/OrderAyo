package com.example.summerproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String prodName;
    private String modifiedBy;
    private String prodType;
    private Long costPrice ;
    private Long sellingPrice;
    private Long availableStock;
}
