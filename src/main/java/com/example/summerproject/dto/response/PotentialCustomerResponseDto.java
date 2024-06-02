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
    String customerEmail;
    String phone;
    String name;
    Long productId;
}
