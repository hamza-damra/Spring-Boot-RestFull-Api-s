package com.hamza.spring.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String name;
    private String email;
    private String body;
    private Set<CommentReplayDto> replies;
}
