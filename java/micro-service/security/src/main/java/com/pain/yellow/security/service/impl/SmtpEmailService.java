package com.pain.yellow.security.service.impl;

import com.pain.yellow.security.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "auth.email-provider", name = "name", havingValue = "smtp")
@RequiredArgsConstructor
@Service
public class SmtpEmailService implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void send(String email, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("service@imooc.com");
        message.setSubject("Spring Security 登录验证码");
        message.setText("验证码为:" + msg);
        emailSender.send(message);
    }
}
