package com.example.summerproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PotentialCustomerResponseDto {
    Long id;
    String productName;
    String customerEmail;
    String customerName;
    String phone;
    Long productId;
}
