package com.hamza.spring.myblog.controller;

import com.hamza.spring.myblog.payload.CreateUserCustomResponse;
import com.hamza.spring.myblog.payload.JwtAuthResponseDto;
import com.hamza.spring.myblog.payload.UserDto;
import com.hamza.spring.myblog.security.CustomUserDetailsServiceImplementation;
import com.hamza.spring.myblog.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final CustomUserDetailsServiceImplementation userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(CustomUserDetailsServiceImplementation userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserCustomResponse> createUser(@Valid @RequestBody UserDto userDto){
        logger.info("Registering user");
        UserDto createdUser = userService.createUser(userDto);
        CreateUserCustomResponse response = new CreateUserCustomResponse("User created successfully", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@RequestParam String username, @RequestParam String password){
        logger.info("User login attempt: " + username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Generate token
            String token = jwtTokenProvider.generateToken(authentication);

            // Return the token in the response body
            return new ResponseEntity<>(new JwtAuthResponseDto(token), HttpStatus.OK);
        } catch (AuthenticationException e) {
            logger.severe("User not authenticated: " + e.getMessage());
            return new ResponseEntity<>(new JwtAuthResponseDto("User not authenticated"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        logger.info("Fetching all users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
