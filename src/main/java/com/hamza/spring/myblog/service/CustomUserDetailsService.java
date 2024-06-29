package com.hamza.spring.myblog.service;

import com.hamza.spring.myblog.payload.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    boolean authenticate(String username, String password);
}
