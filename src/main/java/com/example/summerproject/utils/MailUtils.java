//package com.example.summerproject.utils;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//
//@RequiredArgsConstructor
//@Component
//public class MailUtils {
//    private final JavaMailSender javaMailSender;
//    public static String setTemplet(String to,String subject){
//        String template ="Subject-:"+subject+"\n\n"
//                +"Hello, "+to+"\n\n"
//                + "This is a message just for you to you about the expiry date of ypur renting service, "+to+". ";
//
//        return template;
//    }
//
//    public static String resetTemplet(String to,String subject,String token){
//        String template ="Subject-:"+subject+"\n\n"
//                +"Hello, "+to+"\n\n"
//                + "your reset token is, "+token+". ";
//
//        return template;
//    }
//
//    public String sendMail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        javaMailSender.send(message);
//        return "mail sent";
//    }
//
//}
