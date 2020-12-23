package by.questionary.service;

import by.questionary.domain.User;
import by.questionary.security.payload.request.SignupRequest;

public interface UserService {

    boolean addUser(User user);

    User createUser(SignupRequest signupRequest);

    boolean activateUser(String code);

}
