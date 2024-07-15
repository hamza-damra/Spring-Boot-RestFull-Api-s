package com.hamza.spring.myblog.service.services;

import com.hamza.spring.myblog.payload.ImageDto;

import java.util.List;

public interface ImageService {
    ImageDto createImage(ImageDto imageDto);

    ImageDto getImageById(Long id);

    List<ImageDto> getAllImages();

    ImageDto updateImage(Long id, ImageDto imageDto);

    void deleteImage(Long id);
}
