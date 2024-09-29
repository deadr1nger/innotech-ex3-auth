package ru.inntotech.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.inntotech.auth.model.AppUserDetails;
import ru.inntotech.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails findByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("") || username.equals(null)){
            throw new NullPointerException("Username Id can't be EMPTY or NULL");
        }
        return userRepository.findByUsername(username).map(AppUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s is not found", username)));
    }
}
