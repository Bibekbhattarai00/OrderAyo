package com.example.summerproject.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    Long orderId;

    @NotNull( message = "Customer name cannot be Empty")
    String customerName;

    @Pattern(regexp = "^[0-9]{10}$",message = "invalid phone")
    @NotNull(message = "Mobile-No cannot be empty")
    String customerContact;

    @NotNull(message = "Orders are empty")
    List<OrderItemDto> orderItems;
}
