package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import org.springframework.stereotype.Service;

import javax.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    final UserRepository userRepository;

    public AuthorizationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(Login login) {
        return userRepository.findUserByUsernameAndUserPassword(login.getUsername(), login.getPassword());
    }

    @Override
    public void register(User user) {
        userRepository.save(user);
        sendWelcomeMail(user);
    }

    private void sendWelcomeMail(User user) {
        final String username = "projekt.wardyn@gmail.com";
        final String password = "projektJava1!";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("Thank you for registration");
            message.setText("We are happy you choose our application"
                    + "\n\n Below you can find some information about your account"
                    + "\n\n " + user.toString());

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
