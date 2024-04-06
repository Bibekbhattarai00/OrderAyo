package com.example.summerproject.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    public String sendMail(String to, String subject, String body) ;
}
