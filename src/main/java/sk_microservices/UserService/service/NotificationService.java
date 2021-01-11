package sk_microservices.UserService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.entities.enums.EmailMessage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class NotificationService {

    public void sendMail(String email, EmailMessage enumMessage) {

        String to = "test@example.com";

        String from = "noreply@baeldung.com";
        final String username = "";
        final String password = "";

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            switch (enumMessage){
                case REGISTER:
                    message.setSubject("Email Verification");
                    message.setText("Hi there, please verify your email address");
                    break;
                case EDIT:
                    message.setSubject("Email Verification");
                    message.setText("Hi there, please verify your new email address");
                    break;
                case REFUNDS:
                    message.setSubject("Refunds");
                    message.setText("Hi there, the flight you booked has been canceled");
                    break;
            }

            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
