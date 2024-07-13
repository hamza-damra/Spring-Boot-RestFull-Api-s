package com.hamza.spring.myblog.security;

import com.hamza.spring.myblog.configuration.CustomUserDetails;
import com.hamza.spring.myblog.entity.Role;
import com.hamza.spring.myblog.entity.User;
import com.hamza.spring.myblog.payload.RoleDto;
import com.hamza.spring.myblog.payload.UserDto;
import com.hamza.spring.myblog.repository.RoleRepository;
import com.hamza.spring.myblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticating user: {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Set<GrantedAuthority> authorities = user.get().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        log.info("User authenticated with roles: {}", authorities);
        return new CustomUserDetails(user.get().getUsername(), user.get().getPassword(), authorities);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        log.info("Creating user: {}", userDto.getUsername());
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (RoleDto roleDto : userDto.getRoles()) {
            Role role = roleRepository.findByName("ROLE_" + roleDto.getName().toUpperCase())
                    .orElseThrow(() -> {
                        log.error("Role not found: {}", roleDto.getName());
                        return new RuntimeException("Role not found: " + roleDto.getName());
                    });
            if (user.getRoles().contains(role)) {
                log.error("User already has role: {}", role.getName());
                throw new RuntimeException("User already has role: " + role.getName());
            }
            roles.add(role);
        }

        user.setRoles(roles);
        user = userRepository.save(user);

        log.info("User created: {}", user.getUsername());
        return modelMapper.map(user, UserDto.class);
    }

    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
