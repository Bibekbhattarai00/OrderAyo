package com.example.summerproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderExcelResponseDto {

    Long orderId;

    String customerName;

    String customerContact;

    String productName;

    String productType;

    Long quantity;

    Long price;

    Long total;
}
