package com.hamza.spring.myblog.service.servicesImplementation;

import com.hamza.spring.myblog.entity.Image;
import com.hamza.spring.myblog.entity.Post;
import com.hamza.spring.myblog.exception.ResourceNotFoundException;
import com.hamza.spring.myblog.payload.ImageDto;
import com.hamza.spring.myblog.payload.PostDto;
import com.hamza.spring.myblog.payload.PostResponse;
import com.hamza.spring.myblog.repository.PostRepository;
import com.hamza.spring.myblog.service.services.PostService;
import com.hamza.spring.myblog.validation.markers.OnCreate;
import com.hamza.spring.myblog.validation.markers.OnUpdate;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class PostServiceImplementation implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImplementation(PostRepository postRepository, ModelMapper modelMapper, Validator validator) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public PostDto createPost(@Validated(OnCreate.class) PostDto postDto) {
        Post post = convertToEntity(postDto);
        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);
    }

    @Transactional
    @Override
    public PostResponse getAllPosts(int pageSize, int pageNumber, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostDto> content = postPage.getContent().stream().map(this::mapToDto).collect(Collectors.toList());
        return new PostResponse(postPage.getSize(), postPage.getNumber(), postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast(), content);
    }

    @Override
    public PostDto getPostById(Long id) {
        return mapToDto(postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", String.valueOf(id))));
    }

    @Transactional
    @Override
    public PostDto updatePost(Long id, @Validated(OnUpdate.class) PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", String.valueOf(id)));

        updateEntity(post, postDto);
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("post", "id", String.valueOf(id));
        }
        postRepository.deleteById(id);
    }

    @Override
    public String deleteAllPosts() {
        if (postRepository.count() <= 0) {
            return "No posts to delete";
        } else {
            postRepository.deleteAll();
            return "All posts deleted successfully";
        }
    }

    private PostDto mapToDto(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        Set<ImageDto> imageUrls = post.getImages().stream().map(image -> new ImageDto(image.getUrl())).collect(Collectors.toSet());
        postDto.setImageUrls(imageUrls);
        return postDto;
    }

    private Post convertToEntity(PostDto postDto) {
        Post post = modelMapper.map(postDto, Post.class);
        if (postDto.getImageUrls() != null) {
            Set<Image> images = postDto.getImageUrls().stream().map(imageDto -> {
                Image image = new Image();
                image.setUrl(imageDto.getUrl());
                image.setPost(post);
                return image;
            }).collect(Collectors.toSet());
            post.setImages(images);
        }
        return post;
    }

    private void updateEntity(Post post, PostDto postDto) {
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        if (postDto.getImageUrls() != null) {
            Set<Image> newImages = postDto.getImageUrls().stream().map(imageDto -> {
                Image image = new Image();
                image.setUrl(imageDto.getUrl());
                image.setPost(post);
                return image;
            }).collect(Collectors.toSet());

            // Synchronize access to the post's images collection
            synchronized (post.getImages()) {
                Set<Image> currentImages = new HashSet<>(post.getImages());
                currentImages.forEach(post::removeImage);
                newImages.forEach(post::addImage);
            }
        }
    }
}
