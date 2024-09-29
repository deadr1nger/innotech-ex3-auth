package ru.inntotech.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inntotech.auth.service.AnyExecuteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-execution")
public class UserExecutionController {
    private final AnyExecuteService anyExecuteService;
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String executeByUser(){
        return anyExecuteService.executeForUser();
    }
}
