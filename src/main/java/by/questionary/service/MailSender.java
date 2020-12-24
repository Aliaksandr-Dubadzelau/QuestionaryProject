package by.questionary.service;

import by.questionary.domain.User;

public interface MailSender {

    void sendActivationMail(User user);

    void sendPasswordResetMail(User user);

}
