package com.example.summerproject.auditing;


import com.example.summerproject.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
class AuditorsInfo {

    private final JwtService jwtService;
    private final HttpServletRequest request;
    @Bean
    public AuditorAware<String> auditorProvider() {
        return ()->{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !authentication.equals("anonymousUser")) {
                Object details = authentication.getDetails();
                if (details == null) {
                    return null;
                }
                String authHeader = request.getHeader("Authorization");
                String token = null;
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    return Optional.of(jwtService.extractUsername(token));
                }else{
                    return null;
                }
            } else {
                return null;
            }
        };
    }
}
