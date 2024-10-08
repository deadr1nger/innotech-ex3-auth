package ru.inntotech.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inntotech.auth.model.dto.TokenData;
import ru.inntotech.auth.model.entity.RefreshTokenEntity;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.model.exception.AuthException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenData processPasswordToken(String username, String password) {
        log.info("Start auth process with password");
        if (username  == null || username.equals("")) {
            throw new NullPointerException("Username can't be EMPTY or NULL");
        }
        if (password  == null || password.equals("")) {
            throw new NullPointerException("Password can't be EMPTY or NULL");
        }
        UserEntity user = userService.findByUsername(username);
        log.info("User {} found", user.getUsername());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Exception trying to check password for user: " + username);
        }
        return createTokenData(user);

    }

    @Override
    public TokenData processRefreshToken(UUID refreshTokenValue) {
        log.info("Start auth process with refresh");
        if (refreshTokenValue  == null || refreshTokenValue.equals("")) {
            throw new NullPointerException("RefreshToken id can't be EMPTY or NULL");
        }
        RefreshTokenEntity refreshToken = refreshTokenService.findByValue(refreshTokenValue);
        return createTokenData(userService.findById(refreshToken.getUser().getId()));
    }

    private TokenData createTokenData(UserEntity user) {
        String token = tokenService.generateToken(
                user.getUsername(),
                user.getId(),
                user.getRoles().stream().map(Enum::name).toList());
        RefreshTokenEntity refreshToken = refreshTokenService.saveRefreshToken(user.getId());
        return new TokenData(token, refreshToken.getValue().toString());

    }
}
