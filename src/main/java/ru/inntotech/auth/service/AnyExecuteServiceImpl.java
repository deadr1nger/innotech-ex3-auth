package ru.inntotech.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnyExecuteServiceImpl implements AnyExecuteService {

    @Override
    public String executeForAdmin() {
        log.info("Admin do something");
        return String.format("Admin do something");
    }

    @Override
    public String executeForUser() {
        log.info("User do something");
        return String.format("User do something");
    }
}
