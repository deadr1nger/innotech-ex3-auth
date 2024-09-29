package ru.inntotech.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.inntotech.auth.model.AppUserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class TokenServiceImpl implements TokenService {

    private static final String ROLE_CLAIM = "role";

    private static final String ID_CLAIM = "id";

    @Value("${auth.secret}")
    private String jwtSecret;

    @Value("${auth.auth-token.duration}")
    private Duration tokenExpiration;

    @Override
    public String generateToken(String username, UUID id, List<String> roles) {
        if (username.equals("") || username.equals(null)) {
            throw new NullPointerException("Username can't be EMPTY or NULL");
        }
        if (id.equals("") || id.equals(null)) {
            throw new NullPointerException("Id can't be EMPTY or NULL");
        }
        if (roles.isEmpty()) {
            throw new NullPointerException("Roles can't be EMPTY");
        }
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + tokenExpiration.toMillis())))
                .claim(ROLE_CLAIM, roles)
                .claim(ID_CLAIM, id.toString())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public Authentication toAuthentication(String token) {
        if (token.equals("") || token.equals(null)) {
            throw new NullPointerException("Token can't be EMPTY or NULL");
        }
        Claims tokenBody;
        try {
            tokenBody = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
        String subject = tokenBody.getSubject();
        log.info(subject);
        String id = tokenBody.get(ID_CLAIM, String.class);

        List<String> roles = (List<String>) tokenBody.get(ROLE_CLAIM);
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles can't be EMPTY or NULL");
        }

        log.info(roles.get(0));
        log.info(roles.get(1));

        Principal principal = new AppUserPrincipal(subject, id, roles);
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        return auth;
    }
}
