package com.example.summerproject.dto.request;

import com.example.summerproject.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PotentialCutomerDto {
    Long id;
    String customerEmail;
    String phone;
    String customerName;
    Long productId;
}
