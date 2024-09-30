package ru.inntotech.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.inntotech.auth.model.dto.TokenData;
import ru.inntotech.auth.model.entity.RefreshTokenEntity;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.model.exception.AuthException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private RefreshTokenService refreshTokenService;

    private UserEntity testUser;
    private String username;
    private String password;
    private UUID refreshTokenId;

    @BeforeEach
    void setUp() {
        username = "testUser";
        password = "testPassword";
        refreshTokenId = UUID.randomUUID();

        testUser = new UserEntity();
        testUser.setUsername(username);
        testUser.setPassword(passwordEncoder.encode(password)); // Кодируем пароль для теста
        testUser.setId(UUID.randomUUID());
    }

    @Test
    void processPasswordToken_ShouldThrowException_WhenUsernameIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            securityService.processPasswordToken(null, password);
        });
        assertEquals("Username can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void processPasswordToken_ShouldThrowException_WhenPasswordIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            securityService.processPasswordToken(username, null);
        });
        assertEquals("Password can't be EMPTY or NULL", exception.getMessage());
    }


    @Test
    void processPasswordToken_ShouldThrowException_WhenPasswordIsInvalid() {
        when(userService.findByUsername(username)).thenReturn(testUser);
        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(false);

        Exception exception = assertThrows(AuthException.class, () -> {
            securityService.processPasswordToken(username, password);
        });
        assertEquals("Exception trying to check password for user: testUser", exception.getMessage());
    }

    @Test
    void processRefreshToken_ShouldThrowException_WhenRefreshTokenIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            securityService.processRefreshToken(null);
        });
        assertEquals("RefreshToken id can't be EMPTY or NULL", exception.getMessage());
    }



    @Test
    void processPasswordToken_ShouldReturnTokenData_WhenCredentialsAreValid() {
        when(userService.findByUsername(username)).thenReturn(testUser);
        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(true);


        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(testUser);
        refreshToken.setValue(refreshTokenId);
        when(refreshTokenService.saveRefreshToken(testUser.getId())).thenReturn(refreshToken);

        when(tokenService.generateToken(username, testUser.getId(), testUser.getRoles().stream().map(Enum::name).toList()))
                .thenReturn("token123");

        TokenData tokenData = securityService.processPasswordToken(username, password);

        assertNotNull(tokenData);
        assertEquals("token123", tokenData.getToken());
        assertEquals(refreshTokenId.toString(), tokenData.getRefreshToken());
        verify(userService).findByUsername(username);
        verify(passwordEncoder).matches(password, testUser.getPassword());
        verify(tokenService).generateToken(username, testUser.getId(), testUser.getRoles().stream().map(Enum::name).toList());
        verify(refreshTokenService).saveRefreshToken(testUser.getId());
    }

    @Test
    void processRefreshToken_ShouldReturnTokenData_WhenRefreshTokenIsValid() {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(testUser);
        refreshToken.setValue(refreshTokenId);
        when(refreshTokenService.findByValue(refreshTokenId)).thenReturn(refreshToken);
        when(userService.findById(testUser.getId())).thenReturn(testUser);

        when(tokenService.generateToken(username, testUser.getId(), testUser.getRoles().stream().map(Enum::name).toList()))
                .thenReturn("token123");

        when(refreshTokenService.saveRefreshToken(testUser.getId())).thenReturn(refreshToken);

        TokenData tokenData = securityService.processRefreshToken(refreshTokenId);

        assertNotNull(tokenData);
        assertEquals("token123", tokenData.getToken());
        assertEquals(refreshTokenId.toString(), tokenData.getRefreshToken());
        verify(refreshTokenService).findByValue(refreshTokenId);
        verify(userService).findById(testUser.getId());
        verify(tokenService).generateToken(username, testUser.getId(), testUser.getRoles().stream().map(Enum::name).toList());
    }
}
