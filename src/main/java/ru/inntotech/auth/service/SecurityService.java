package ru.inntotech.auth.service;

import ru.inntotech.auth.model.dto.TokenData;
import ru.inntotech.auth.model.entity.UserEntity;

import java.util.UUID;

public interface SecurityService {

    public TokenData processPasswordToken(String username, String password);
    public TokenData processRefreshToken(UUID refreshTokenValue);

}
