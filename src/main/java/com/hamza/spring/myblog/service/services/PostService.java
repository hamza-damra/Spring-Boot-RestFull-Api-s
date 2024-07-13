package com.hamza.spring.myblog.service.services;

import com.hamza.spring.myblog.payload.PostDto;
import com.hamza.spring.myblog.payload.PostResponse;
import com.hamza.spring.myblog.validation.markers.OnCreate;
import org.springframework.validation.annotation.Validated;

@Validated
public interface PostService {
    @Validated(OnCreate.class)
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageSize, int pageNumber, String sortBy, String sortDirection);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id,@Validated PostDto postDto);

    void deletePost(Long id);

    String deleteAllPosts();
}
