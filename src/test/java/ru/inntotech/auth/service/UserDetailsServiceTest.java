package ru.inntotech.auth.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.inntotech.auth.model.AppUserDetails;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private String username;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        username = "testUser";
        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");
        user.setPassword("password123");
    }


    @Test
    void findByUsername_ShouldThrowException_WhenUsernameIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> userDetailsService.findByUsername(null));
        assertEquals("Username Id can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUsernameIsEmpty() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> userDetailsService.findByUsername(""));
        assertEquals("Username Id can't be EMPTY or NULL", exception.getMessage());
    }

    @Test
    void findByUsername_ShouldReturnUserDetails_WhenUserExists() {

        AppUserDetails appUserDetails = new AppUserDetails(user); // Предположим, что у вас есть конструктор по умолчанию


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.findByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.findByUsername("unknownUser"));
        assertTrue(exception.getMessage().contains("User with username unknownUser is not found"));
    }
}
