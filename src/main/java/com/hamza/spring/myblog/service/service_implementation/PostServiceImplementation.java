package com.hamza.spring.myblog.service.service_implementation;

import com.hamza.spring.myblog.entity.Comment;
import com.hamza.spring.myblog.entity.Post;
import com.hamza.spring.myblog.exception.ResourceNotFoundException;
import com.hamza.spring.myblog.payload.CommentDto;
import com.hamza.spring.myblog.payload.PostDto;
import com.hamza.spring.myblog.payload.PostResponse;
import com.hamza.spring.myblog.repository.CommentRepository;
import com.hamza.spring.myblog.repository.PostRepository;
import com.hamza.spring.myblog.service.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImplementation implements PostService {
    PostRepository postRepository;
    ModelMapper modelMapper;

    @Autowired
    public PostServiceImplementation(PostRepository postRepository, ModelMapper modelMapper, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public PostDto createPost(PostDto postDto) {
        // convert PostDto to Post entity
        Post post = convertToEntity(postDto);

        // save post to database
        Post savedPost = postRepository.save(post);
        PostDto postDtoResponse = mapToDto(savedPost);

        if(savedPost.getComments() == null) {
            postDtoResponse.setComments(new HashSet<>());
        }
        // convert Post entity to PostDto and return it as response
        return postDtoResponse;
    }

    @Transactional
    @Override
    public PostResponse getAllPosts(int pageSize, int pageNumber, String sortBy, String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostDto> content = postPage.getContent().stream().map(this::mapToDto).toList();

        return new PostResponse(postPage.getSize(), postPage.getNumber(), postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast(), content);
    }

    @Override
    public PostDto getPostById(Long id) {
        return mapToDto(postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post","id",String.valueOf(id))));
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto) {
        // fetch post from database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post","id",String.valueOf(id)));

        // update post details
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        // save updated post to database
        Post updatedPost = postRepository.save(post);

        // convert Post entity to PostDto and return it as response
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("post","id",String.valueOf(id));
        }
        postRepository.deleteById(id);
    }

    private PostDto mapToDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }

    private Post convertToEntity(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }

    private CommentDto commentToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }


}
