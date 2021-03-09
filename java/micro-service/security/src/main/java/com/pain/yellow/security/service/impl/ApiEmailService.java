package com.pain.yellow.security.service.impl;

import com.pain.yellow.security.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@ConditionalOnProperty(prefix = "auth.email-provider", name = "name", havingValue = "api")
@RequiredArgsConstructor
@Service
public class ApiEmailService implements EmailService {

    private final SendGrid sendGrid;

    @Override
    public void send(String email, String msg) {
        Email from = new Email("service@security.com");
        String subject = "Spring Security 登录验证码";
        Email to = new Email(email);
        Content content = new Content("text/plain", "验证码为:" + msg);
        Mail mail = new Mail(from, subject, to, content);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("邮件发送成功");
            } else {
                log.error(response.getBody());
            }
        } catch (IOException e) {
            log.error("请求发生异常 {}", e.getLocalizedMessage());
        }
    }
}
