package com.hamza.spring.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private int pageSize;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private List<CommentDto> content;
}
