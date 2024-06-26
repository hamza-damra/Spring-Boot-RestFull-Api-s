package com.hamza.spring.myblog.service;

import com.hamza.spring.myblog.payload.PostDto;
import com.hamza.spring.myblog.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageSize, int PageNumber, String sortBy, String sortDirection);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto);

    void deletePost(Long id);
}
