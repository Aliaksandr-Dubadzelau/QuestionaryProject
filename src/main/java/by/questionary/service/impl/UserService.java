package by.questionary.service.impl;

import by.questionary.domain.User;
import by.questionary.repository.UserRepository;
import by.questionary.service.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
