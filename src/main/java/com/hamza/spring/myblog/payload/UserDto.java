package com.hamza.spring.myblog.payload;

import com.hamza.spring.myblog.validation.UniqueUsername;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotEmpty(message = "Username is required")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters long")
    @UniqueUsername
    private String username;
    @NotEmpty(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 5 and 100 characters long")
    private String password;
    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 5 and 100 characters long")
    private String name;
    @NotEmpty(message = "Role is required")
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
