package ru.inntotech.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

import javax.crypto.SecretKey;
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
        if (username == null || username.equals("")) {
            throw new NullPointerException("Username can't be EMPTY or NULL");
        }
        if (id == null || id.equals("")) {
            throw new NullPointerException("Id can't be EMPTY or NULL");
        }
        if (roles.isEmpty()) {
            throw new NullPointerException("Roles can't be EMPTY");
        }
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis())).claim(ROLE_CLAIM, roles).claim(ID_CLAIM, id.toString()).signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public Authentication toAuthentication(String token) {
        if (token == null || token.equals("")) {
            throw new NullPointerException("Token can't be EMPTY or NULL");
        }
        Claims tokenBody;
        try {
            tokenBody = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }

        String subject = tokenBody.getSubject();
        String id = tokenBody.get(ID_CLAIM, String.class);
        List<String> roles = tokenBody.get(ROLE_CLAIM, List.class);
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles can't be EMPTY or NULL");
        }
        Principal principal = new AppUserPrincipal(subject, id, roles);
        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
}
