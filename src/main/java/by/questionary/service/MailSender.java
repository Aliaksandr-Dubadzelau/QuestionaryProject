package by.questionary.service;

import by.questionary.domain.User;

import java.util.concurrent.CompletableFuture;

public interface MailSender {

    CompletableFuture<Boolean> sendActivationMail(User user);

    CompletableFuture<Boolean> sendPasswordResetMail(User user);

}
