package ru.inntotech.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inntotech.auth.service.AnyExecuteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/execution")
@Slf4j
public class ExecutionController {

    private final AnyExecuteService anyExecuteService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Method for admin")
    @GetMapping("/admin")
    public String executeByAdmin() {
        return anyExecuteService.executeForAdmin();
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Method for regular user")
    @GetMapping("/user")
    public String executeByUser() {
        return anyExecuteService.executeForUser();
    }
}
