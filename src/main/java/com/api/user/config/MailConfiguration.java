package com.api.user.config;

import com.api.user.define.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
@Configuration
public class MailConfiguration {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(Constant.MailConfiguration.MAIL_HOST);
        mailSender.setPort(Constant.MailConfiguration.MAIL_PORT);

        mailSender.setUsername(Constant.MailConfiguration.MAIL_USERNAME);
        mailSender.setPassword(Constant.MailConfiguration.MAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.socketFactory.port", Constant.MailConfiguration.MAIL_SMTP_SOCKET_FACTORY_PORT);
        props.put("mail.smtp.auth", Constant.MailConfiguration.MAIL_SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", Constant.MailConfiguration.MAIL_SMTP_STARTTLS_ENABLE);
        props.put("mail.smtp.ssl.trust", Constant.MailConfiguration.MAIL_SMTP_SSL_TRUST);
        props.put("mail.smtp.socketFactory.class", Constant.MailConfiguration.MAIL_SMTP_SOCKET_FACTORY_CLASS);
        props.put("mail.smtp.socketFactory.fallback", Constant.MailConfiguration.MAIL_SMTP_SOCKETFACTORY_FALLBACK);
        props.put("mail.store.protocol", Constant.MailConfiguration.MAIL_STORE_PROTOCOL);
        props.put("mail.transport.protocol", Constant.MailConfiguration.MAIL_TRANSPORT_PROTOCOL);
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}
