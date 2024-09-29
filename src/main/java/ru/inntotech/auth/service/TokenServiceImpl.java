package ru.inntotech.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.inntotech.auth.model.AppUserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
        Claims tokenBody = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        String subject = tokenBody.getSubject();
        String id = tokenBody.get(ID_CLAIM, String.class);
        List<String> roles = (List<String>) tokenBody.get(ROLE_CLAIM);

        Principal principal = new AppUserPrincipal(subject, id, roles);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, roles.stream().map(SimpleGrantedAuthority::new).toList());
        return auth;
    }
}
