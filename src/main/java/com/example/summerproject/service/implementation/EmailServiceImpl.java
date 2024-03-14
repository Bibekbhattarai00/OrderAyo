package com.example.summerproject.service.implementation;

import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final CustomMessageSource messageSource;

    @Override
    public String sendMail(String to, String subject, String body) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            return messageSource.get(ExceptionMessages.MAIL_SENT.getCode());
    }
}
