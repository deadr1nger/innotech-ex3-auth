package ru.inntotech.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inntotech.auth.model.entity.UserEntity;
import ru.inntotech.auth.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity createUser(UserEntity user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return user;

    }

    @Override
    public UserEntity findById(UUID userId) {
        if (userId.equals("") || userId.equals(null)) {
            throw new NullPointerException("User Id can't be EMPTY or NULL");
        }
        return userRepository.findById(userId).orElseThrow(() -> new NullPointerException(String.format("User with Id %s is not found", userId)));
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s is not found", username)));
    }
}
