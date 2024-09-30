package ru.inntotech.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inntotech.auth.model.AppUserDetails;
import ru.inntotech.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails findByUsername(String username) throws UsernameNotFoundException {
        if (username  == null || username.equals("")) {
            throw new NullPointerException("Username Id can't be EMPTY or NULL");
        }
        return userRepository.findByUsername(username).map(AppUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s is not found", username)));
    }
}
