package by.questionary.service;

import by.questionary.domain.User;

public interface MessageCreator {

    String createActivationEmailMessage(User user);

    String createPasswordResetMessage(User user);
}
