package com.hamza.spring.myblog.payload;
import com.hamza.spring.myblog.validation.UniqueTitle;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;

    @UniqueTitle(message = "Title must be unique value")
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters long")
    private String title;
    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters long")
    private String description;
    @NotEmpty(message = "Content cannot be empty")
    @Size(min = 100, max = 10000, message = "Content must be between 100 and 10,000 characters long")
    private String content;
    private Set<CommentDto> comments;
}
