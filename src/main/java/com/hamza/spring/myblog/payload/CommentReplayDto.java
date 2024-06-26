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
    @Size(message = "Comment replay name must be between 10 and 50 characters long", min = 10, max = 50)
    private String name;
    @NotEmpty(message = "Comment replay email cannot be empty")
    @Size(message = "Comment replay email must be between 5 and 50 characters long", min = 5, max = 50)
    @Email(message = "Comment replay email must be a valid email address")
    private String email;
    @NotEmpty(message = "Comment replay body cannot be empty")
    @Size(message = "Comment replay body must be between 10 and 500 characters long", min = 10, max = 500)
    private String body;
}
