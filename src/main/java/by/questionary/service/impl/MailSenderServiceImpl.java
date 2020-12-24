package by.questionary.service.impl;

import by.questionary.domain.User;
import by.questionary.service.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MailSenderServiceImpl implements MailSender {

    private static final String ACTIVATION_MAIL_SUBJECT = "Activation code";
    private static final String PASSWORD_RESET_MAIL_SUBJECT = "Password reset code code";
    private static final boolean ADDED = true;
    private static final boolean NOT_ADDED = false;

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;
    private final MessageCreatorServiceImpl messageCreatorServiceImpl;

    public MailSenderServiceImpl(JavaMailSender mailSender, MessageCreatorServiceImpl messageCreatorServiceImpl) {
        this.mailSender = mailSender;
        this.messageCreatorServiceImpl = messageCreatorServiceImpl;
    }

    private void send(String mailTo, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        log.info("Mail {} has been sent", mailMessage);

        mailSender.send(mailMessage);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> sendActivationMail(User user) {

        boolean result = NOT_ADDED;

        String message = messageCreatorServiceImpl.createActivationEmailMessage(user);
        String userEmail = user.getEmail();

        log.info("Data {}, {} for mail(activation) is created", message, userEmail);

        if (!StringUtils.isEmpty(userEmail)) {
            send(userEmail, ACTIVATION_MAIL_SUBJECT, message);
            result = ADDED;
        }

        return CompletableFuture.completedFuture(result);
    }

    @Async
    @Override
    public CompletableFuture<Boolean>  sendPasswordResetMail(User user) {

        boolean result = NOT_ADDED;

        String message = messageCreatorServiceImpl.createPasswordResetMessage(user);
        String userEmail = user.getEmail();

        log.info("Data {}, {} for mail(reset password) is created", message, userEmail);

        if (!StringUtils.isEmpty(userEmail)) {
            send(userEmail, PASSWORD_RESET_MAIL_SUBJECT, message);
            result = ADDED;
        }

        return CompletableFuture.completedFuture(result);
    }
}
