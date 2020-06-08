package com.accommodation.system.service.impl;


import com.accommodation.system.service.MailSendingService;
import com.accommodation.system.uitls.AESUtil;
import com.accommodation.system.uitls.HtmlUtil;
import com.accommodation.system.utils2.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Multipart;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class MailSendingServiceImpl implements MailSendingService {
    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void mailConfirmRegister(String email, String fullName, int userId) throws Exception {
        String content = HtmlUtil.createReportMailTemplate("template/template-confirm.html", null);
        String link = "http://localhost:9000/user/confirm-register?id=" + AESUtil.encrypt(userId + "");
        content = content.replaceAll("__link__", link)
                .replaceAll("__Fullname__ ", fullName);
        MailUtil.send("CONFIRM REGISTER","huongnq4@gmail.com",null,null,content,null);
        log.info("Sent mail confirm register to {} success!!!!", email);
    }

    @Override
    public void mailResetPassword(String email, String display_name, String newPassword) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        InternetHeaders headers = new InternetHeaders();
        headers.addHeader("Content-type", "text/html; charset=UTF-8");

        String content = HtmlUtil.createReportMailTemplate("template/template-reset.html", null);
        content = content.replaceAll("__PASSWORD__", newPassword)
                .replaceAll("__Fullname__ ", display_name);
        MimeBodyPart body = null;
        try {
            body = new MimeBodyPart(headers, content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        body.setText(content, "UTF-8", "html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);

        message.setContent(multipart);
        helper.setFrom("quanghuongitus@gmail.com");
        helper.setTo(email);
        helper.setSubject("RESET PASSWORD");
        this.emailSender.send(message);
        log.info("Sent mail reset password to {} success!", email);
    }

}
