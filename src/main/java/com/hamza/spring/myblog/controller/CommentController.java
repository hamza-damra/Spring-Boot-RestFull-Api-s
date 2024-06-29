package com.hamza.spring.myblog.controller;

import com.hamza.spring.myblog.payload.CommentDto;
import com.hamza.spring.myblog.payload.CommentResponse;
import com.hamza.spring.myblog.service.CommentService;
import com.hamza.spring.myblog.utils.AppConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{post_id}")
    ResponseEntity<Object> createComment(@PathVariable(name = "post_id") long postId, @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/post/{post_id}")
    ResponseEntity<CommentResponse> getAllComments(@PathVariable(name = "post_id") long postId,
                                                   @RequestParam(name = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                   @RequestParam(name = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                   @RequestParam(name = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_FIELD, required = false) String sortBy,
                                                   @RequestParam(name = "sortDirection", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        return new ResponseEntity<>(commentService.getAllCommentsByPostId(postId, pageSize, pageNumber, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("/{comment_id}")
    ResponseEntity<CommentDto> getCommentById(@PathVariable(name = "comment_id") Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }

    @PutMapping("/{post_id}/{comment_id}")
    ResponseEntity<CommentDto> updateComment(@PathVariable(name = "post_id") Long postId, @PathVariable(name = "comment_id") Long commentId, @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateComment(postId, commentId, commentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{comment_id}")
    ResponseEntity<Object> deleteComment(@PathVariable(name = "comment_id") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("Comment with id " + commentId + " deleted successfully", HttpStatus.OK);
    }
}
