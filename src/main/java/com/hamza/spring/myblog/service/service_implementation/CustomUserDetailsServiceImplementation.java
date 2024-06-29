package com.hamza.spring.myblog.service.service_implementation;

import com.hamza.spring.myblog.configuration.CustomUserDetails;
import com.hamza.spring.myblog.entity.Role;
import com.hamza.spring.myblog.entity.User;
import com.hamza.spring.myblog.payload.RoleDto;
import com.hamza.spring.myblog.payload.UserDto;
import com.hamza.spring.myblog.repository.RoleRepository;
import com.hamza.spring.myblog.repository.UserRepository;
import com.hamza.spring.myblog.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
public class CustomUserDetailsServiceImplementation implements CustomUserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticating user: {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = user.get().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new CustomUserDetails(user.get().getUsername(), user.get().getPassword(), authorities);
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (RoleDto roleDto : userDto.getRoles()) {
            Role role = roleRepository.findByName("ROLE_" + roleDto.getName().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleDto.getName()));
            if (user.getRoles().contains(role)) {
                throw new RuntimeException("User already has role: " + role.getName());
            }

            roles.add(role);
        }

        user.setRoles(roles);
        user = userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}
