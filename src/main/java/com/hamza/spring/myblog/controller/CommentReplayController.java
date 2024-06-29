package com.hamza.spring.myblog.controller;

import com.hamza.spring.myblog.entity.Comment;
import com.hamza.spring.myblog.payload.CommentDto;
import com.hamza.spring.myblog.payload.CommentReplayDto;
import com.hamza.spring.myblog.service.CommentReplayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment-replays")
public class CommentReplayController {

    CommentReplayService commentReplayService;

    @Autowired
    public CommentReplayController(CommentReplayService commentReplayService) {
        this.commentReplayService = commentReplayService;
    }

    @PostMapping("/{comment_id}")
    public ResponseEntity<Object> createCommentReplay(@PathVariable Long comment_id,@Valid @RequestBody CommentReplayDto commentReplayDto) {
        if (commentReplayDto.getBody() == null) {
            return ResponseEntity.badRequest().body("Body field is required.");
        }
        commentReplayService.createCommentReplay(comment_id, commentReplayDto);
        return ResponseEntity.ok("Comment replay created successfully for comment with id: " + comment_id);
    }

    @GetMapping("/{comment_id}")
    public ResponseEntity<Iterable<CommentReplayDto>> getAllCommentReplays(@PathVariable Long comment_id) {
        return ResponseEntity.ok(commentReplayService.getAllCommentReplays(comment_id));
    }


}
