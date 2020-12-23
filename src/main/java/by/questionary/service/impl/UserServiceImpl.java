package by.questionary.service.impl;

import by.questionary.domain.Role;
import by.questionary.domain.User;
import by.questionary.repository.UserRepository;
import by.questionary.security.payload.request.SignupRequest;
import by.questionary.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final boolean ACTIVATED = true;
    private static final boolean NOT_ACTIVATED = false;
    private static final boolean ADDED = true;
    private static final boolean NOT_ADDED = false;

    private static final String CONFIRMED_ACTIVATION_CODE = null;
    private static final String MAIL_SUBJECT = "Activation code";

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UUIDServiceImpl uuidServiceImpl;
    private final MailSenderServiceImpl mailSenderServiceImpl;
    private final MessageCreatorServiceImpl messageCreatorServiceImpl;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("User \" " + name + "\" not found.");
                    throw new UsernameNotFoundException("User \" " + name + "\" not found.");
                });
    }

    public boolean existsUserByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean existsUserByEmail(String name) {
        return userRepository.existsByEmail(name);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean addUser(User user) {

        String userEmail = user.getEmail();
        String userName = user.getName();

        boolean existedUserByEmail = existsUserByEmail(userEmail);
        boolean existedUserByName = existsUserByName(userName);

        boolean added = NOT_ADDED;

        if (!existedUserByEmail && !existedUserByName && !StringUtils.isEmpty(user.getEmail())) {

            added = ADDED;
            String message = messageCreatorServiceImpl.createActivationEmailMessage(user);

            mailSenderServiceImpl.send(userEmail, MAIL_SUBJECT, message);

        }

        log.info("User {} saved - {}", user, added);

        return added;
    }

    @Override
    public User createUser(SignupRequest signupRequest) {

        User user = new User();

        String activationCode = uuidServiceImpl.createUUID();
        String userEmail = signupRequest.getEmail();
        String userName = signupRequest.getName();
        String userPassword = encoder.encode(signupRequest.getPassword());

        user.setName(userName);
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActivationCode(activationCode);
        user.setRegistrationDate(new Date());
        user.setActivated(NOT_ACTIVATED);
        user.setPasswordCode(0);

        log.info("User {} is created", user);

        return user;
    }

    @Override
    public boolean activateUser(String code) {

        User user = userRepository.findBYActivationCode(code).orElseThrow();
        boolean activated = ACTIVATED;

        if (user == null) {
            activated = NOT_ACTIVATED;
        } else {
            activate(user);
            saveUser(user);
        }

        log.info("User {} is activated - {}", user, activated);

        return activated;
    }

    private void activate(User user) {

        log.info("Activate");

        user.setActivated(ACTIVATED);
        user.setActivationCode(CONFIRMED_ACTIVATION_CODE);
    }
}
