package com.hamza.spring.myblog.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCustomResponse {
    private String message;
    private UserDto user;
}

