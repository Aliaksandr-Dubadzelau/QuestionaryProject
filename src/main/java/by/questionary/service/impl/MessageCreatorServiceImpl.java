package by.questionary.service.impl;

import by.questionary.domain.User;
import by.questionary.service.MessageCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MessageCreatorServiceImpl implements MessageCreator {

    @Override
    public String createActivationEmailMessage(User user) {

        String emailMessage = "Something go wrong";

        if (user != null && !StringUtils.isEmpty(user.getEmail())) {
            emailMessage = String.format(
                    "Hello, %s! "
                            + "\n"
                            + "Welcome to Questionary. Please, visit our link: http://localhost:8080/activate/%s",
                    user.getName(),
                    user.getActivationCode());
        }

        log.info("New activation e-mail is created: {}", emailMessage);

        return emailMessage;

    }
    
    @Override
    public String createPasswordResetMessage(User user) {
        return null;
    }
}
