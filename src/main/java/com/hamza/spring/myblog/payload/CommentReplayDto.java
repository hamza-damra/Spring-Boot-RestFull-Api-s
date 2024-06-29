package com.hamza.spring.myblog.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReplayDto {
    private Long id;
    @NotEmpty(message = "Comment replay name cannot be empty")
    @Size(message = "Comment replay name must be between 10 and 50 characters long")
    private String name;
    @NotEmpty(message = "Comment replay email cannot be empty")
    @Email(message = "Comment replay email must be a valid email address")
    private String email;
    @NotEmpty(message = "Comment replay body cannot be empty")
    @Size(min = 2, max = 500, message = "Comment replay body must be between 10 and 500 characters long")
    private String body;
}
