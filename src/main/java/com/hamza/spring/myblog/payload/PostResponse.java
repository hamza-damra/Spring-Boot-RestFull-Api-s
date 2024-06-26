package com.hamza.spring.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private int pageSize;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private List<PostDto> content;
}
