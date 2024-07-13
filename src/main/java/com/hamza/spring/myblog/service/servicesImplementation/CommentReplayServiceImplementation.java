package com.hamza.spring.myblog.service.servicesImplementation;

import com.hamza.spring.myblog.entity.Comment;
import com.hamza.spring.myblog.entity.CommentReplay;
import com.hamza.spring.myblog.exception.ResourceNotFoundException;
import com.hamza.spring.myblog.payload.CommentReplayDto;
import com.hamza.spring.myblog.repository.CommentReplayRepository;
import com.hamza.spring.myblog.repository.CommentRepository;
import com.hamza.spring.myblog.service.services.CommentReplayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CommentReplayServiceImplementation implements CommentReplayService  {

    CommentRepository commentRepository;
    CommentReplayRepository commentReplayRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentReplayServiceImplementation(CommentReplayRepository commentReplayRepository,CommentRepository commentRepository,  ModelMapper modelMapper) {
        this.commentReplayRepository = commentReplayRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createCommentReplay(Long commentId, CommentReplayDto commentReplayDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id", commentId.toString()));

        CommentReplay commentReplay = mapCommentReplayDtoToComment(commentReplayDto);
        commentReplay.setComment(comment); // Link the reply to the comment

        // Save the comment reply
        commentReplayRepository.save(commentReplay);

        // Add the reply to the comment's replies list
        comment.getReplies().add(commentReplay);
    }

    @Override
    public Iterable<CommentReplayDto> getAllCommentReplays(Long commentId) {
        // find all comments for the given comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id", commentId.toString()));
        return comment.getReplies().stream().map(this::mapCommentToCommentReplayDto).collect(Collectors.toList());
    }

    private CommentReplayDto mapCommentToCommentReplayDto(CommentReplay commentReplay) {
        return modelMapper.map(commentReplay, CommentReplayDto.class);
    }

    private CommentReplay mapCommentReplayDtoToComment(CommentReplayDto commentReplayDto) {
        return modelMapper.map(commentReplayDto, CommentReplay.class);
    }


}
