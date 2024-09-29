package ru.inntotech.auth.util.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ServerWebExchange;
import ru.inntotech.auth.model.exception.AuthException;
import ru.inntotech.auth.service.TokenService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityAuthConverter implements AuthenticationConverter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Override
    public Authentication convert(HttpServletRequest request) {
        return extractBearerToken(request)
                .map(tokenService::toAuthentication)
                .orElse(null);
    }

    private Optional<String> extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)){
            throw new AuthException("Authorization header is broken");
        }
        return Optional.of(authorizationHeader.substring(BEARER_PREFIX.length()));
    }
}
