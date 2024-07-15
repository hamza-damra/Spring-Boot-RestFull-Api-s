package com.hamza.spring.myblog.payload;

import com.hamza.spring.myblog.validation.annotations.UniqueUsername;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters long")
    @UniqueUsername
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long")
    private String password;

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters long")
    private String name;

    @NotEmpty(message = "At least one role is required")
    private Set<RoleDto> roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
