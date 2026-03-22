package com.fivepigs.app.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailService {

    private static final Properties emailProperties = new Properties();

    static {
        try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find email.properties");
            } else {
                emailProperties.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email properties", e);
        }
    }

    private static final String FROM_EMAIL = emailProperties.getProperty("email.from");
    private static final String PASSWORD = emailProperties.getProperty("email.password");

    public static void sendEmail(String toEmail, String subject, String body) {
        sendHtmlEmail(toEmail, subject, "<p>" + body + "</p>");
    }

    public static void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
