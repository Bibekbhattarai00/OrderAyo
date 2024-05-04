package com.example.summerproject.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    Long prodId;

    @NotNull(message = "Product Cannot be empty")
    String name;

    @Positive(message = "Stock cannot be Negative Or 0")
    @NotNull(message = "stock Cannot be empty")
    Long stock;

    @NotNull(message = "Product Type Cannot be empty")
    String prodType;

    @Positive(message = "Selling Price cannot be Negative Or 0")
    @NotNull(message = "Selling price Cannot be empty")
    Long sellingPrice;

    @Positive(message = "Cost Price cannot be Negative Or 0")
    @NotNull(message = "cost price Cannot be empty")
    Long costPrice;

}