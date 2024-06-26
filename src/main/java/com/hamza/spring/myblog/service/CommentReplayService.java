package com.hamza.spring.myblog.service;


import com.hamza.spring.myblog.payload.CommentDto;
import com.hamza.spring.myblog.payload.CommentReplayDto;

public interface CommentReplayService {
    void createCommentReplay(Long commentId, CommentReplayDto commentReplayDto);
}
