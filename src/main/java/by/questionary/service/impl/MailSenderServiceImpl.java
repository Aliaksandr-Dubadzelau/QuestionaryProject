package by.questionary.service.impl;

import by.questionary.domain.User;
import by.questionary.service.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailSenderServiceImpl implements MailSender {

    private static final String ACTIVATION_MAIL_SUBJECT = "Activation code";
    private static final String PASSWORD_RESET__MAIL_SUBJECT = "Password reset code code";

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;
    private final MessageCreatorServiceImpl messageCreatorServiceImpl;

    public MailSenderServiceImpl(JavaMailSender mailSender, MessageCreatorServiceImpl messageCreatorServiceImpl) {
        this.mailSender = mailSender;
        this.messageCreatorServiceImpl = messageCreatorServiceImpl;
    }

    private void send(String mailTo, String subject, String message) {

        log.info("Mail has been sent");

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @Async
    @Override
    public void sendActivationMail(User user) {

        String message = messageCreatorServiceImpl.createActivationEmailMessage(user);
        String userEmail = user.getEmail();

        log.info("Data {}, {} for mail(activation) is created", message, userEmail);

        send(userEmail, ACTIVATION_MAIL_SUBJECT, message);
    }

    @Async
    @Override
    public void sendPasswordResetMail(User user) {

        String message = messageCreatorServiceImpl.createPasswordResetMessage(user);
        String userEmail = user.getEmail();

        log.info("Data {}, {} for mail(reset password) is created", message, userEmail);

        send(userEmail, PASSWORD_RESET__MAIL_SUBJECT, message);
    }
}
