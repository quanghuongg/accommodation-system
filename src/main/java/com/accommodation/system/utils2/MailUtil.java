package com.accommodation.system.utils2;

import viettel.vtcc.common.conf.ConfigurationLoader;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class MailUtil {
    private static Session getSession() throws IOException {
        Properties prop = ConfigurationLoader.getInstance().getProperties();

        String username = prop.getProperty("mail.smtp.auth.username");
        String password = prop.getProperty("mail.smtp.auth.password");
        return Session.getDefaultInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void send(String subject, String toUsers, String ccUsers, String bccUsers, String content) throws IOException, MessagingException {
        send(subject, toUsers, ccUsers, bccUsers, content, null);
    }

    public static void send(String subject, String toUsers, String ccUsers, String bccUsers, String content, String[] files) throws MessagingException, IOException {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setRecipients(Message.RecipientType.TO, toUsers);
        if (Utils.isNotEmpty(ccUsers)) {
            message.setRecipients(Message.RecipientType.CC, ccUsers);
        }
        if (Utils.isNotEmpty(bccUsers)) {
            message.setRecipients(Message.RecipientType.BCC, bccUsers);
        }


        InternetHeaders headers = new InternetHeaders();
        headers.addHeader("Content-type", "text/html; charset=UTF-8");
        MimeBodyPart body = new MimeBodyPart(headers, content.getBytes("UTF-8"));
        body.setText(content, "UTF-8", "html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);
        message.setContent(multipart);
        if (null != files) {
            for (String file : files) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(file);
                multipart.addBodyPart(attachmentBodyPart);
            }
        }
        message.setContent(multipart);
        message.setFrom(new InternetAddress(
                ConfigurationLoader.getInstance().getAsString("mail.smtp.auth.username",""),
                ConfigurationLoader.getInstance().getAsString("mail.smtp.from.alias","Reputa")));
        Transport.send(message);
    }

    public static void send(String subject, String toUsers, String ccUsers, String content) throws MessagingException, IOException {
        send(subject, toUsers, ccUsers, null, content);
    }

    public static void send(String subject, String toUsers, String content) throws MessagingException, IOException {
        send(subject, toUsers, null, null, content);
    }

    public static void send(String subject, String toUsers, String ccUsers, String content, String[] files) throws MessagingException, IOException {
        send(subject, toUsers, ccUsers, null, content, files);
    }

    public static void send(String subject, String toUsers, String content, String[] files) throws MessagingException, IOException {
        send(subject, toUsers, null, null, content, files);
    }
}
