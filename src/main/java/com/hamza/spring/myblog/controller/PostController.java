package com.hamza.spring.myblog.controller;

import com.hamza.spring.myblog.payload.PostDto;
import com.hamza.spring.myblog.payload.PostResponse;
import com.hamza.spring.myblog.service.services.PostService;
import com.hamza.spring.myblog.utils.AppConstant;
import com.hamza.spring.myblog.validation.markers.OnCreate;
import com.hamza.spring.myblog.validation.markers.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<PostDto> createPost(@Validated(OnCreate.class) @RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        notifyClients(createdPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(name = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(name = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                    @RequestParam(name = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_FIELD, required = false) String sortBy,
                                                    @RequestParam(name = "sortDirection", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        return new ResponseEntity<>(postService.getAllPosts(pageSize, pageNumber, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id, @Validated(OnUpdate.class) @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(id, postDto);
        notifyClients(updatedPost);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("Post with id " + id + " deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Object> deleteAllPosts() {
        String message = postService.deleteAllPosts();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/sse")
    public SseEmitter streamPosts() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    private void notifyClients(PostDto postDto) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("new-post").data(postDto));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }
}
