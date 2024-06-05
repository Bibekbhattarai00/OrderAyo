package com.example.summerproject.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    Long id;
    @NotNull(message = "Product Cannot be empty")
    Long productId;

    @Positive(message = "Quantity cannot be negative or Zero")
    Long quantity;
}