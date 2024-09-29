package ru.inntotech.auth.service;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface TokenService {

    public String generateToken(String username, UUID id, List<String> roles);

    public Authentication toAuthentication(String token);
}
