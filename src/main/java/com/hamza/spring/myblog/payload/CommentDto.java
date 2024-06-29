package com.hamza.spring.myblog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(message = "Content cannot be empty")
    @Size(min = 2, max = 500, message = "Content must")
    private String name;
    @NotEmpty(message = "Content cannot be empty")
    @Email
    private String email;
    @NotEmpty(message = "Content cannot be empty")
    @Size(min = 2, max = 500, message = "Content must be between 10 and 500 characters long")
    private String body;
    private Set<CommentReplayDto> replies;
}
