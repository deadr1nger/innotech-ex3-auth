package ru.inntotech.auth.service;


import ru.inntotech.auth.model.entity.RefreshTokenEntity;

import java.util.UUID;

public interface RefreshTokenService {

    public RefreshTokenEntity saveRefreshToken(UUID userId);

    public RefreshTokenEntity findByValue(UUID value);
}
