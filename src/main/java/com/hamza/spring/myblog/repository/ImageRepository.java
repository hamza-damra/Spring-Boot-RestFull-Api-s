package com.hamza.spring.myblog.repository;

import com.hamza.spring.myblog.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
