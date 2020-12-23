package by.questionary.service.impl;

import by.questionary.service.UUIDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UUIDServiceImpl implements UUIDService {

    @Override
    public String createUUID() {
        log.info("UUID is created");
        return UUID.randomUUID().toString();
    }

}
