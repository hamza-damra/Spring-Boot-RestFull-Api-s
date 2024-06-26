package com.hamza.spring.myblog.security;

import com.hamza.spring.myblog.payload.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers();
}
