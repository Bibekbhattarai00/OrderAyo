package com.example.summerproject.service;

import com.example.summerproject.dto.request.ForgotPasswordDto;
import com.example.summerproject.dto.request.PasswordResetDto;
import com.example.summerproject.dto.request.ResetTokenDto;
import jakarta.servlet.http.HttpServletRequest;

public interface PasswordResetService {
     String requestReset(HttpServletRequest request, PasswordResetDto passwordResetDto);
     String forgotPassword( ForgotPasswordDto forgotPasswordDto);

     String reset(ResetTokenDto resetTokenDto);

}
