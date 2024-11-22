package com.example.summerproject.dto.request;

import com.example.summerproject.generic.dto.PaginationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequestDto extends PaginationRequest {
    String prodType;
    String name="-1";
}
