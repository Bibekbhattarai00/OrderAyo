package com.example.summerproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdnameResponseDto {
    private Long prodId;

    String productName;

    String productType;

    Long quantity;

    Long price;
}
