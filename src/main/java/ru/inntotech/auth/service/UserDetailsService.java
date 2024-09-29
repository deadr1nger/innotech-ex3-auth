package ru.inntotech.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {

    public UserDetails findByUsername(String username);
}
