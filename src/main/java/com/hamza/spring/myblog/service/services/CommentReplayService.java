package com.hamza.spring.myblog.service.services;


import com.hamza.spring.myblog.payload.CommentReplayDto;

public interface CommentReplayService {
    void createCommentReplay(Long commentId, CommentReplayDto commentReplayDto);

    Iterable<CommentReplayDto> getAllCommentReplays(Long commentId);
}
