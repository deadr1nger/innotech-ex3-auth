package ru.inntotech.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inntotech.auth.model.entity.RefreshTokenEntity;
import ru.inntotech.auth.repository.RefreshTokenRepository;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${auth.refresh-token.duration}")
    private Duration refreshTokenDuration;

    @Override
    @Transactional
    public RefreshTokenEntity saveRefreshToken(UUID userId) {
        if(userId.equals("") || userId.equals(null)){
            throw new NullPointerException("User Id can't be EMPTY or NULL");
        }
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(userService.findById(userId));
        refreshTokenEntity.setValue(UUID.randomUUID());
        refreshTokenEntity.setRefreshTokenExpiration(refreshTokenDuration);
        refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity;
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshTokenEntity findByValue(UUID value) throws NullPointerException {
        if(value.equals("") || value.equals(null)){
            throw new NullPointerException("Token value can't be EMPTY or NULL");
        }
        refreshTokenRepository.findByValue(value);
        return refreshTokenRepository.findByValue(value).orElseThrow(() -> new NullPointerException(String.format("RefreshToken with value: %s is not found", value)));
    }
}
