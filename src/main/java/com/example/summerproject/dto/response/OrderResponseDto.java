package com.example.summerproject.dto.response;

import com.example.summerproject.entity.Product;
import com.example.summerproject.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    Long orderId;

    String customerName;

    String customerContact;

    List<ProdnameResponseDto> products;

    Long total;
}
