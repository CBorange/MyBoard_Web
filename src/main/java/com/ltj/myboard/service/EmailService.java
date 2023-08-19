package com.ltj.myboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class EmailService {

    @Value("${server.connect-url}")
    private String serverConnectUrl;

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public void sendFindUserConfirmMail(String toMail, String confirmLinkId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("MyBoardService <info@ltj-myboard.kro.kr>");
        helper.setTo(toMail);
        helper.setSubject("MyBoard 계정 찾기 확인");

        String html = parseMailTemplate(toMail, confirmLinkId);
        helper.setText(html, true);

        mailSender.send(message);
    }

    public String parseMailTemplate(String toMail, String linkParam){
        // Process Tyhmeleaf Mail Template
        Context context = new Context();
        context.setVariable("toMail", toMail);
        context.setVariable("linkParam", linkParam);
        context.setVariable("urlPath", serverConnectUrl);

        String html = templateEngine.process("find-user-mail.html", context);
        return html;
    }
}
