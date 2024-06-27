package com.hamza.spring.myblog.service.service_implementation;

import com.hamza.spring.myblog.configuration.CustomUserDetails;
import com.hamza.spring.myblog.entity.User;
import com.hamza.spring.myblog.payload.UserDto;
import com.hamza.spring.myblog.repository.UserRepository;
import com.hamza.spring.myblog.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImplementation implements CustomUserDetailsService {

    private final UserRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticating user: {}", username);
        Optional<User> user = userDetailsRepository.findByUsername(username);
        log.info("User found: {}", user);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        String password = user.get().getPassword();
        log.info("password {}", password);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.get().getRole()));
        log.info("roles {}",authorities);
        return new CustomUserDetails(user.get().getUsername(), password, authorities);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_" + userDto.getRole().toUpperCase());
        user = userDetailsRepository.save(user);
        UserDto savedUserDto = modelMapper.map(user, UserDto.class);
        userDto.setId(user.getId());
        userDto.setCreatedAt(user.getCreatedAt());
        return savedUserDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
       List<User> users =  userDetailsRepository.findAll();
       return  users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }
}
