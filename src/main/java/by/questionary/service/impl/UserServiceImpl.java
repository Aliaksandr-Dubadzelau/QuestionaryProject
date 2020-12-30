package by.questionary.service.impl;

import by.questionary.domain.Role;
import by.questionary.domain.User;
import by.questionary.repository.UserRepository;
import by.questionary.security.payload.request.SignupRequest;
import by.questionary.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final boolean ACTIVATED = true;
    private static final boolean NOT_ACTIVATED = false;

    private static final String CONFIRMED_ACTIVATION_CODE = null;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UUIDServiceImpl uuidServiceImpl;

    public boolean existsUserByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean existsUserByEmail(String name) {
        return userRepository.existsByEmail(name);
    }

    public User saveUser(User user) {

        log.info("User {} is saved", user.getName());

        return userRepository.save(user);
    }

    @Override
    public boolean comparePasswords(String password, String repeatedPassword) {
        return password.equals(repeatedPassword);
    }

    @Override
    public User createUserByRequest(SignupRequest signupRequest) {

        User user = new User();

        String userEmail = signupRequest.getEmail();
        String userName = signupRequest.getName();
        String userPassword = encoder.encode(signupRequest.getPassword());

        user.setName(userName);
        user.setEmail(userEmail);
        user.setPassword(userPassword);

        log.info("User {} is created", user.getName());

        return user;
    }

    @Override
    public User prepareUserToSaving(User user) {

        String activationCode = uuidServiceImpl.createUUID();

        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActivationCode(activationCode);
        user.setRegistrationDate(new Date());
        user.setActivated(NOT_ACTIVATED);

        log.info("User {} is prepared to saving", user.getName());

        return user;
    }

    @Override
    public boolean activateUser(String code) {

        boolean activated = NOT_ACTIVATED;

        Optional<User> user = userRepository.findByActivationCode(code);

        if (user.isPresent()) {
            activate(user.get());
            saveUser(user.get());
            activated = ACTIVATED;
        }

        log.info("Code {} is activated - {}", code, activated);

        return activated;
    }

    private void activate(User user) {

        user.setActivated(ACTIVATED);
        user.setActivationCode(CONFIRMED_ACTIVATION_CODE);
    }
}
