package ru.inntotech.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.inntotech.auth.model.dto.TokenData;
import ru.inntotech.auth.model.dto.TokenResponse;
import ru.inntotech.auth.model.dto.PasswordTokenPostRequest;
import ru.inntotech.auth.model.dto.RefreshTokenRequest;
import ru.inntotech.auth.service.SecurityService;
import ru.inntotech.auth.model.mapper.TokenDataMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TokenControllerTest {

    @InjectMocks
    private TokenController tokenController;

    @Mock
    private SecurityService securityService;

    @Mock
    private TokenDataMapper tokenDataMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void password_ShouldReturnTokenResponse_WhenCredentialsAreValid() {
        PasswordTokenPostRequest request = new PasswordTokenPostRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");

        TokenData tokenData = new TokenData("tokenValue", "refreshTokenValue");
        TokenResponse expectedResponse = new TokenResponse("tokenValue", "refreshTokenValue");

        when(securityService.processPasswordToken(request.getUsername(), request.getPassword())).thenReturn(tokenData);
        when(tokenDataMapper.tokenDataToTokenResponse(tokenData)).thenReturn(expectedResponse);

        TokenResponse actualResponse = tokenController.password(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void refresh_ShouldReturnTokenResponse_WhenRefreshTokenIsValid() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(UUID.randomUUID());

        TokenData tokenData = new TokenData("newTokenValue", "newRefreshTokenValue");
        TokenResponse expectedResponse = new TokenResponse("newTokenValue", "newRefreshTokenValue");

        when(securityService.processRefreshToken(any())).thenReturn(tokenData);
        when(tokenDataMapper.tokenDataToTokenResponse(tokenData)).thenReturn(expectedResponse);

        TokenResponse actualResponse = tokenController.refresh(request);

        assertEquals(expectedResponse, actualResponse);
    }
}
