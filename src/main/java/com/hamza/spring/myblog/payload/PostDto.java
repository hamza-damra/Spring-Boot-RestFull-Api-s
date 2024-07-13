package com.hamza.spring.myblog.payload;

import com.hamza.spring.myblog.validation.markers.OnCreate;
import com.hamza.spring.myblog.validation.markers.OnUpdate;
import com.hamza.spring.myblog.validation.annotations.UniqueTitle;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;

    @UniqueTitle(message = "Title must be unique", groups = OnCreate.class)
    @NotEmpty(message = "Title cannot be empty", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters long", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotEmpty(message = "Description cannot be empty", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters long", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotEmpty(message = "Content cannot be empty", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 3, max = 500, message = "Content must be between 3 and 500 characters long", groups = {OnCreate.class, OnUpdate.class})
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<CommentDto> comments;
    private Set<ImageDto> imageUrls;
}
