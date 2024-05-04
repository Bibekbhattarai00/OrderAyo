package com.example.summerproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponseDto {
    Long id;
    String email;
    String role;
    String addedBy;
}
