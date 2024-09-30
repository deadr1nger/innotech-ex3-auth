package ru.inntotech.auth.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AnyExecuteServiceImplTest {

    private final AnyExecuteServiceImpl anyExecuteService = new AnyExecuteServiceImpl();

    @Test
    void executeForAdmin_ShouldReturnExpectedMessage() {
        String result = anyExecuteService.executeForAdmin();
        assertEquals("Admin do something", result);
    }

    @Test
    void executeForUser_ShouldReturnExpectedMessage() {
        String result = anyExecuteService.executeForUser();
        assertEquals("User do something", result);
    }
}