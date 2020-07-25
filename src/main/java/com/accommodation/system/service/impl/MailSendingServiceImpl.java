package com.accommodation.system.service.impl;


import com.accommodation.system.service.MailSendingService;
import com.accommodation.system.uitls.AESUtil;
import com.accommodation.system.uitls.HtmlUtil;
import com.accommodation.system.uitls.MailUtil;
import com.accommodation.system.uitls.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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

    @Async("threadPoolTaskExecutor")
    @Override
    public void mailConfirmRegister(String email, String fullName, int userId) throws Exception {
        String content = HtmlUtil.createReportMailTemplate("template/template-confirm.html", null);
        String link = "http://54.169.144.80:3000/user/confirm?id=" + AESUtil.encrypt(userId + "");
        content = content.replaceAll("__link__", link)
                .replaceAll("__Fullname__ ", fullName);
        MailUtil.send("CONFIRM REGISTER", "huongnq4@gmail.com", null, null, content, null);
        log.info("Sent mail confirm register to {} success!!!!", email);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void mailToAdmin(String userFeedBack, String userPost, String userId, String postId, String content) throws Exception {
        String contentMail = HtmlUtil.createReportMailTemplate("template/template-admin.html", null);
        contentMail = contentMail.
                replaceAll("__USER_FEEDBACK_", userFeedBack)
                .replaceAll("__USERPOST__", userPost)
                .replaceAll("__UserId__", userId)
                .replaceAll("__CONTENT__ ", content)
                .replaceAll("__PostId__ ", postId);
        MailUtil.send("PHẢN HỒI TỪ NGƯỜI DÙNG", "huongnq4@gmail.com", null, null, contentMail, null);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void mailFeedback(String displayName, String email) throws Exception {
        String contentMail = HtmlUtil.createReportMailTemplate("template/template-feedback.html", null);
        contentMail = contentMail.
                replaceAll("__USERNAME__", displayName);
        if (Utils.isEmpty(email) || true) {
            email = "huongnq4@gmail.com";
        }
        MailUtil.send("BÁO CÁO VI PHẠM", email, null, null, contentMail, null);
    }
}
