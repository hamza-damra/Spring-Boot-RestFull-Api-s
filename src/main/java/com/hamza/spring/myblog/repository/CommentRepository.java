package com.hamza.spring.myblog.repository;

import com.hamza.spring.myblog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> getCommentsByPostId(long postId, Pageable pageable);
}
