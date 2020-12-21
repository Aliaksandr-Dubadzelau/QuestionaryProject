package by.questionary.security.service;

import by.questionary.domain.User;
import by.questionary.repository.UserRepository;
import by.questionary.security.entity.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        User user = userRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("User \" " + name + "\" not found.");
                    throw new UsernameNotFoundException("User \" " + name + "\" not found.");
                });

        return UserDetailsImpl.of(user);
    }
}
