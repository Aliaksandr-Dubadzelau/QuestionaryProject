package by.questionary.service;

import by.questionary.domain.User;
import by.questionary.security.payload.request.SignupRequest;

public interface UserService {

    User createUserByRequest(SignupRequest signupRequest);

    User prepareUserToSaving(User user);

    boolean activateUser(String code);

}
