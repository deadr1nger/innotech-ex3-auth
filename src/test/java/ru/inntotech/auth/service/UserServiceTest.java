package ru.inntotech.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");
        user.setPassword("password123");
    }

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(encodedPassword, createdUser.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void findById_ShouldThrowException_WhenUserIdIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> userService.findById(null));
        assertEquals("User Id can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    void findById_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> userService.findById(UUID.randomUUID()));
        assertTrue(exception.getMessage().contains("User with Id"));
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        String username = "unknownUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(username));
        assertTrue(exception.getMessage().contains("User with username"));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }
}
