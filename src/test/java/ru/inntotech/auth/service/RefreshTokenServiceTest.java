package ru.inntotech.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import ru.inntotech.auth.model.entity.RefreshTokenEntity;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.repository.RefreshTokenRepository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceImplTest {

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

    private UUID userId;
    private UserEntity testUser;
    private RefreshTokenEntity refreshTokenEntity;

    @Value("${auth.refresh-token.duration}")
    private Duration refreshTokenDuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        testUser = new UserEntity();
        testUser.setId(userId);
        testUser.setUsername("testUser");

        refreshTokenDuration = Duration.ofDays(30); // Установите продолжительность токена
        refreshTokenEntity = new RefreshTokenEntity();
    }

    @Test
    void saveRefreshToken_ShouldReturnRefreshTokenEntity_WhenUserIdIsValid() {
        when(userService.findById(userId)).thenReturn(testUser);

        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(refreshTokenEntity);

        RefreshTokenEntity result = refreshTokenService.saveRefreshToken(userId);

        assertNotNull(result);
        assertNotNull(result.getValue());
        assertEquals(testUser, result.getUser());

        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
    }

    @Test
    void saveRefreshToken_ShouldThrowException_WhenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            refreshTokenService.saveRefreshToken(null);
        });
    }

    @Test
    void findByValue_ShouldReturnRefreshTokenEntity_WhenValueIsValid() {
        UUID tokenValue = UUID.randomUUID();
        refreshTokenEntity.setValue(tokenValue);
        refreshTokenEntity.setUser(testUser);

        when(refreshTokenRepository.findByValue(tokenValue)).thenReturn(Optional.of(refreshTokenEntity));

        RefreshTokenEntity result = refreshTokenService.findByValue(tokenValue);

        assertNotNull(result);
        assertEquals(tokenValue, result.getValue());
        assertEquals(testUser, result.getUser());
    }

    @Test
    void findByValue_ShouldThrowException_WhenTokenValueIsNull() {
        assertThrows(NullPointerException.class, () -> {
            refreshTokenService.findByValue(null);
        });
    }

    @Test
    void findByValue_ShouldThrowException_WhenTokenIsNotFound() {
        UUID tokenValue = UUID.randomUUID();

        when(refreshTokenRepository.findByValue(tokenValue)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            refreshTokenService.findByValue(tokenValue);
        });
    }
}
