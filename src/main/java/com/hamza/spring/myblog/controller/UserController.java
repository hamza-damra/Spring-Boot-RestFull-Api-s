package com.hamza.spring.myblog.controller;


import com.hamza.spring.myblog.payload.CreateUserCustomResponse;
import com.hamza.spring.myblog.payload.UserDto;
import com.hamza.spring.myblog.service.service_implementation.CustomUserDetailsServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    CustomUserDetailsServiceImplementation userService;

    @Autowired
    public UserController(CustomUserDetailsServiceImplementation userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserCustomResponse> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUser = userService.createUser(userDto);
        CreateUserCustomResponse response = new CreateUserCustomResponse("User created successfully", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // get All Users
    @GetMapping("/all")
    ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // make login function using username and password
    @GetMapping("/login")
    public ResponseEntity<Boolean> login(@RequestParam String username, @RequestParam String password){
       boolean isAuthenticated =  userService.authenticate(username, password);
        if(isAuthenticated){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }





}
