package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.ForgotPasswordDto;
import com.example.summerproject.dto.request.PasswordResetDto;
import com.example.summerproject.dto.request.ResetTokenDto;
import com.example.summerproject.entity.ResetTokenEntity;
import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.jwt.JwtService;
import com.example.summerproject.repo.ResetTokenRepo;
import com.example.summerproject.repo.UserEntityRepo;
import com.example.summerproject.securityconfig.UserInfoDetailService;
import com.example.summerproject.service.PasswordResetService;
import com.example.summerproject.utils.MailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final JwtService jwtService;
    private final UserInfoDetailService userInfoDetailService;
    private final UserEntityRepo userEntityRepo;
    private final PasswordEncoder passwordEncoder;
    private final ResetTokenRepo resetTokenRepo;
    private final MailUtils mailUtils;
    private final CustomMessageSource messageSource;

    @Override
    public String requestReset(HttpServletRequest request, PasswordResetDto passwordResetDto) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails userDetails = userInfoDetailService.loadUserByUsername(jwtService.extractUsername(token));
            String newPassword = passwordEncoder.encode(passwordResetDto.getNewPassword());
            if (passwordEncoder.matches(passwordResetDto.getOldPassword(), userDetails.getPassword())) {
                UserEntity user = userEntityRepo.findByUsername(jwtService.extractUsername(token))
                        .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
                user.setPassword(newPassword);
                userEntityRepo.save(user);
                return messageSource.get(ExceptionMessages.SUCCESS.getCode());
            } else {
                throw new NotFoundException(messageSource.get(ExceptionMessages.INVALID_CREDENTIALS.getCode()));
            }
        } else {
            throw new NotFoundException(messageSource.get(ExceptionMessages.AUTHENTICATION_ERROR.getCode()));
        }

    }

    @Transactional
    @Override
    public String forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        String username = forgotPasswordDto.getUsername();
        UserEntity user = userEntityRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        String to = user.getUsername();
        String sub = "reset token";
        String resetToken = otpGenerator();
        int resetTokenEntity = resetTokenRepo.userCount(username);

        if(resetTokenEntity!=1) {
            saveResetToken(user.getUsername(), resetToken);
        }else{
            resetTokenRepo.updateToken(user.getUsername(), resetToken);
        }

        String emailBody = MailUtils.resetTemplet(to, sub, resetToken);
        mailUtils.sendMail(to, sub, emailBody);
        return messageSource.get(ExceptionMessages.MAIL_SENT.getCode());
    }

    public String reset(ResetTokenDto resetTokenDto) {
        ResetTokenEntity resetToken = resetTokenRepo.findByUsername(resetTokenDto.getUsername())
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));

        String username = resetToken.getUsername();

        UserEntity user = userEntityRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));

        if (resetTokenDto.getToken().equals(resetToken.getToken())) {
            String password = passwordEncoder.encode(resetTokenDto.getPassword());
            user.setPassword(password);
            userEntityRepo.save(user);
        } else {
            throw new NotFoundException(messageSource.get(ExceptionMessages.INVALID_CREDENTIALS.getCode()));
        }
        resetTokenRepo.delete(resetToken);

        return messageSource.get(ExceptionMessages.SUCCESS.getCode());
    }

    void saveResetToken(String username, String token) {
        ResetTokenEntity resetTokenEntity = new ResetTokenEntity();
        resetTokenEntity.setUsername(username);
        resetTokenEntity.setToken(token);
        resetTokenRepo.save(resetTokenEntity);
    }

    public String otpGenerator() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
