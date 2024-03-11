package com.example.summerproject.dto.request;

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
public class ResetTokenDto {
    Long id;

    @NotNull(message = "username Cannot be empty")
    @Email(message = "Email format mismatched")
    String username;

    @NotNull(message = "username Cannot be empty")
    String token;

    @NotNull(message = "username Cannot be empty")
    String password;
}
