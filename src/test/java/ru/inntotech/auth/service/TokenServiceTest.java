package ru.inntotech.auth.service;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Value("${auth.secret}")
    private String jwtSecret = "mysecretkeymysecretkeymysecretkeymysecretkey"; // Для тестов
    private SecretKey key;
    private String username;
    private UUID userId;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        username = "testUser";
        userId = UUID.randomUUID();
        roles = new ArrayList<>(Collections.singletonList("ROLE_USER"));
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Test
    void generateToken_ShouldThrowException_WhenUsernameIsNull() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> tokenService.generateToken(null, userId, roles));
        assertEquals("Username can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void generateToken_ShouldThrowException_WhenUserIdIsNull() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> tokenService.generateToken(username, null, roles));
        assertEquals("Id can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void generateToken_ShouldThrowException_WhenRolesAreEmpty() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> tokenService.generateToken(username, userId, new ArrayList<>()));
        assertEquals("Roles can't be EMPTY", exception.getMessage());
    }

    @Test
    void toAuthentication_ShouldThrowException_WhenTokenIsNull() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> tokenService.toAuthentication(null));
        assertEquals("Token can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void toAuthentication_ShouldThrowException_WhenTokenIsEmpty() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> tokenService.toAuthentication(""));
        assertEquals("Token can't be EMPTY or NULL", exception.getMessage());
    }



}
