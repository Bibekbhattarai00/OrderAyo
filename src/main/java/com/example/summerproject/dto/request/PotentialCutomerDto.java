package com.example.summerproject.dto.request;

import com.example.summerproject.entity.Product;
import jakarta.validation.constraints.Email;
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
    @Email(message = "invalid email format")
    String customerEmail;
    String phone;
    String customerName;
    Long productId;
}
