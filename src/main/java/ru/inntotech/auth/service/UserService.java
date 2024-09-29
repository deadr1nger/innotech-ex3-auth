package ru.inntotech.auth.service;

import ru.inntotech.auth.model.entity.UserEntity;

import java.util.UUID;

public interface UserService {

    public UserEntity createUser(UserEntity user);

    public UserEntity findById(UUID userId);

    public UserEntity findByUsername(String username);

}
