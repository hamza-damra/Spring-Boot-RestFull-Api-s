package com.hamza.spring.myblog.repository;

import com.hamza.spring.myblog.entity.CommentReplay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplayRepository extends JpaRepository<CommentReplay, Long> {
}
