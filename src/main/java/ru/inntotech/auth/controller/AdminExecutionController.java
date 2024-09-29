package ru.inntotech.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inntotech.auth.service.AnyExecuteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-execution")
public class AdminExecutionController {

    private final AnyExecuteService anyExecuteService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String executeByAdmin(){
        return anyExecuteService.executeForAdmin();
    }
}
