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
public class PasswordResetDto {
    @NotNull(message = "old password field empty")
    String oldPassword;
    @NotNull(message = "new password field empty")
    String newPassword;
}
