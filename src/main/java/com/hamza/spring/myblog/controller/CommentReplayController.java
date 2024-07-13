package com.hamza.spring.myblog.controller;

import com.hamza.spring.myblog.payload.CommentReplayDto;
import com.hamza.spring.myblog.service.services.CommentReplayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/comment-replays")
public class CommentReplayController {

    private final CommentReplayService commentReplayService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    public CommentReplayController(CommentReplayService commentReplayService) {
        this.commentReplayService = commentReplayService;
    }

    @PostMapping("/{comment_id}")
    public ResponseEntity<Object> createCommentReplay(@PathVariable Long comment_id, @Valid @RequestBody CommentReplayDto commentReplayDto) {
        if (commentReplayDto.getBody() == null) {
            return ResponseEntity.badRequest().body("Body field is required.");
        }
        commentReplayService.createCommentReplay(comment_id, commentReplayDto);
        notifyClients(commentReplayDto);
        return new ResponseEntity<>("Comment replay created successfully for comment id: " + comment_id, HttpStatus.CREATED);
    }

    @GetMapping("/{comment_id}")
    public ResponseEntity<Iterable<CommentReplayDto>> getAllCommentReplays(@PathVariable Long comment_id) {
        return ResponseEntity.ok(commentReplayService.getAllCommentReplays(comment_id));
    }

    @GetMapping("/sse/{comment_id}")
    public SseEmitter streamCommentReplays(@PathVariable Long comment_id) {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    private void notifyClients(CommentReplayDto commentReplayDto) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("new-comment-replay").data(commentReplayDto));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }
}
