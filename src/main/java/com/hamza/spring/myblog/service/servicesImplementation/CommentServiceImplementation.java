package com.hamza.spring.myblog.service.servicesImplementation;

import com.hamza.spring.myblog.entity.Comment;
import com.hamza.spring.myblog.entity.Post;
import com.hamza.spring.myblog.exception.BlogApiException;
import com.hamza.spring.myblog.exception.ResourceNotFoundException;
import com.hamza.spring.myblog.payload.CommentDto;
import com.hamza.spring.myblog.payload.CommentResponse;
import com.hamza.spring.myblog.repository.CommentRepository;
import com.hamza.spring.myblog.repository.PostRepository;
import com.hamza.spring.myblog.service.services.CommentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImplementation implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = convertToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id", String.valueOf(postId)));
        comment.setPost(post);
        System.out.println("Before saving: " + comment);
        comment = commentRepository.save(comment);
        System.out.println("After saving: " + comment);
        return convertToDto(comment);
    }

    @Override
    public CommentResponse getAllCommentsByPostId(long postId, int pageSize, int PageNumber, String sortBy, String sortDirection) {
        // check sort direction if asc or desc
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // create Pageable object for pagination
        Pageable pageable = PageRequest.of(PageNumber, pageSize, sort);

        // find all posts in the given page
        Page<Comment> commentPage = commentRepository.getCommentsByPostId(postId, pageable);


        // get content of the page as list of Post entities
        List<Comment> comments = commentPage.getContent();

        // convert Post entities to PostDtos
        List<CommentDto> content = comments.stream().map(this::convertToDto).toList();

        return new CommentResponse(commentPage.getSize(),
                commentPage.getNumber(), commentPage.getTotalElements(),
                commentPage.getTotalPages(), commentPage.isLast(),content);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        return convertToDto(commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment","id", String.valueOf(id))));

    }

    @Override
    public CommentDto updateComment(Long postId, Long id, CommentDto commentDto) {
        // Fetch the post and comment from their respective repositories
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(id)));

        // Verify that the comment belongs to the specified post
        if (!comment.getPost().getId().equals(postId)) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the given post");
        }

        // Update the comment with new data
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());


        // Save the updated comment and convert it to DTO
        Comment updatedComment = commentRepository.save(comment);
        return convertToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        if(!commentRepository.existsById(id)){
            throw new ResourceNotFoundException("Comment","id",String.valueOf(id));
        }
        commentRepository.deleteById(id);
    }


    private CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment convertToEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}
