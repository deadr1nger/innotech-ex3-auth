package ru.inntotech.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inntotech.auth.service.AnyExecuteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-execution")
@Slf4j
public class AdminExecutionController {

    private final AnyExecuteService anyExecuteService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exec")
    public String executeByAdmin() {
        log.info("exec");
        return anyExecuteService.executeForAdmin();
    }
}
