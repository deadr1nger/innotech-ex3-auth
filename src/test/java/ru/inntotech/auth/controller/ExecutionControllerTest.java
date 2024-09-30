package ru.inntotech.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.inntotech.auth.service.AnyExecuteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExecutionControllerTest {

    @InjectMocks
    private ExecutionController adminExecutionController;

    @Mock
    private AnyExecuteService anyExecuteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executeByAdmin_ShouldReturnAdminMessage_WhenUserIsAdmin() {
        String expectedMessage = "Admin do something";
        when(anyExecuteService.executeForAdmin()).thenReturn(expectedMessage);

        setUpSecurityContext("ADMIN");

        String actualMessage = adminExecutionController.executeByAdmin();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void executeByUser_ShouldReturnUserMessage_WhenUserIsUser() {
        String expectedMessage = "User do something";
        when(anyExecuteService.executeForUser()).thenReturn(expectedMessage);

        setUpSecurityContext("USER");

        String actualMessage = adminExecutionController.executeByUser();

        assertEquals(expectedMessage, actualMessage);
    }

    private void setUpSecurityContext(String role) {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
