package com.hamza.spring.myblog.controller;

import com.hamza.spring.myblog.payload.PostDto;
import com.hamza.spring.myblog.payload.PostResponse;
import com.hamza.spring.myblog.service.service_implementation.PostServiceImplementation;
import com.hamza.spring.myblog.utils.AppConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    PostServiceImplementation postService;

    @Autowired
    public PostController(PostServiceImplementation postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
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
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id,@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("post with id " + id + " deleted successfully", HttpStatus.OK);
    }
}
