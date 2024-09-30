package ru.inntotech.auth.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inntotech.auth.model.dto.PasswordTokenPostRequest;
import ru.inntotech.auth.model.dto.RefreshTokenRequest;
import ru.inntotech.auth.model.dto.TokenResponse;
import ru.inntotech.auth.model.mapper.TokenDataMapper;
import ru.inntotech.auth.service.SecurityService;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenDataMapper tokenDataMapper;
    private final SecurityService securityService;
    @PostMapping("/password")
    public TokenResponse password(@RequestBody PasswordTokenPostRequest passwordTokenRequest) {
        log.info(passwordTokenRequest.getUsername());
        return tokenDataMapper.tokenDataToTokenResponse(securityService.processPasswordToken(passwordTokenRequest.getUsername(), passwordTokenRequest.getPassword()));
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        return tokenDataMapper.tokenDataToTokenResponse(securityService.processRefreshToken(request.getRefreshToken()));

    }
}
