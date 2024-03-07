package com.example.summerproject.controller;


import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.ForgotPasswordDto;
import com.example.summerproject.dto.request.ResetTokenDto;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reset-password")
@Tag(name = "Forgot password Controller", description = "APIs for Forgot password")
@RequiredArgsConstructor
public class ForgotPasswordController extends BaseController {

    private final PasswordResetService passwordResetService;


    @Operation(summary = "Forgot password generate Otp", description = "Generate Otp for password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otp sent"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/generate-Otp")
    public GenericResponse<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        return successResponse(passwordResetService.forgotPassword(forgotPasswordDto),"password reset token sent");
    }
    @Operation(summary = "Password reset forgot password", description = "Allows user to reset their password using the token provided by system to users in theri email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset success"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/reset")
    public GenericResponse<String> resetPassword(@RequestBody ResetTokenDto resetTokenDto){
        return successResponse(passwordResetService.reset(resetTokenDto),"password reset successfully");
    }
}

