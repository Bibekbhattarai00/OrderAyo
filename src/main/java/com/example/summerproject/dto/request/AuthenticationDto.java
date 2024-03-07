package com.example.summerproject.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto {
    @NotNull(message = "username cannot be null")
    String username;
    @NotNull(message = "password cannot be null")
    String password;
}
