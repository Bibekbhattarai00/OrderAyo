package com.example.summerproject.dto.request;

import com.example.summerproject.entity.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long userId;

    @Email(message = "please enter a valid email")
    String username;
    @NotNull(message = "password cannot be null")
    String password;
    String userType;
}