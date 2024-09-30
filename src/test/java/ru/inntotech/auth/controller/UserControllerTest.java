package ru.inntotech.auth.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.inntotech.auth.model.dto.UserPostRequest;
import ru.inntotech.auth.model.dto.UserResponse;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.model.mapper.UserMapper;
import ru.inntotech.auth.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Test
    void createUser_ShouldReturnUserResponse_WhenUserIsCreated() {
        // Arrange
        UserPostRequest request = new UserPostRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setEmail("test@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");
        userEntity.setEmail("test@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("testUser");
        userResponse.setEmail("test@example.com");

        when(userMapper.userDtoToEntity(request)).thenReturn(userEntity);
        when(userService.createUser(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.entityToUserDTO(userEntity)).thenReturn(userResponse);

        UserResponse response = userController.createUser(request);

        assertEquals(userResponse, response);
        assertEquals("testUser", response.getUsername());
    }
}
