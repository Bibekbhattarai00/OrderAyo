package com.example.summerproject.securityconfig;

import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;


@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final JwtService jwtService;
    private final UserInfoDetailService userInfoDetailService;
    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            String errorMessage = "";
            if (authException instanceof BadCredentialsException) {
                errorMessage = "Invalid username or password.";
            } else if (authException instanceof DisabledException) {
                errorMessage = "Your account has been disabled. Please contact support.";
            } else if (authException instanceof LockedException) {
                errorMessage = "Your account has been locked due to too many failed attempts. Please try again later.";
            } else if (authException instanceof AccountExpiredException) {
                errorMessage = "Your account has expired. Please contact support to renew your account.";
            } else if (authException instanceof CredentialsExpiredException) {
                errorMessage = "Your credentials have expired. Please update your password.";
            } else {
                errorMessage = "Authentication failed";
            }

            GenericResponse<Object> genericResponse = GenericResponse.builder()
                    .success(false)
                    .message("Exception Thrown")
                    .data(errorMessage)
                    .build();

            // Convert the GenericResponse to JSON and write it to the response
            String jsonResponse = new ObjectMapper().writeValueAsString(genericResponse);
            writer.println(jsonResponse);
        } catch (IOException e) {
            // Handle exception if there's an issue writing to the response
            throw new ServletException("Failed to send unauthorized response to the client", e);
        }
    }
}
