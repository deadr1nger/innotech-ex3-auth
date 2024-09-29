package ru.inntotech.auth.controller;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inntotech.auth.model.dto.UserPostRequest;
import ru.inntotech.auth.model.dto.UserResponse;
import ru.inntotech.auth.model.mapper.UserMapper;
import ru.inntotech.auth.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PermitAll
    @PostMapping("/register")
    public UserResponse createUser(@RequestBody UserPostRequest request) {
        return userMapper.entityToUserDTO(userService.createUser(userMapper.userDtoToEntity(request)));
    }
}
