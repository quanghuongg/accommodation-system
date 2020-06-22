package com.accommodation.system.uitls;
import com.accommodation.system.uitls.ConfigurationLoader;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailUtil {
    static final int PORT = 465;
    private static Session getSession() throws IOException {
        Properties props = ConfigurationLoader.getInstance().getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        String username = "nguyenquanghuongg@gmail.com";
        String password = "talbgypyekmppflz";
        return Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
    static final String FROM = "huongnq4@gmail.com";
    static final String FROMNAME = "Sender Name";

    public static void send(String subject, String toUsers, String ccUsers, String bccUsers, String content, String[] files) throws MessagingException, IOException {
        Session session = getSession();
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toUsers));
        msg.setSubject(subject);
        msg.setContent(content, "text/html; charset=UTF-8");

        try {
            System.out.println("Sending...");
            Transport.send(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
    }
}
