package com.hamza.spring.myblog.service;


import com.hamza.spring.myblog.payload.CommentDto;
import com.hamza.spring.myblog.payload.CommentResponse;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    CommentResponse getAllCommentsByPostId(long postId, int pageSize, int PageNumber, String sortBy, String sortDirection);

    CommentDto getCommentById(Long id);

    CommentDto updateComment(Long postId, Long id, CommentDto commentDto);

    void deleteComment(Long id);


}
