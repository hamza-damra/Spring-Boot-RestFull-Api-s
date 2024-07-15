package com.hamza.spring.myblog.service.servicesImplementation;

import com.hamza.spring.myblog.entity.Image;
import com.hamza.spring.myblog.exception.ResourceNotFoundException;
import com.hamza.spring.myblog.payload.ImageDto;
import com.hamza.spring.myblog.repository.ImageRepository;
import com.hamza.spring.myblog.service.services.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImplementation implements ImageService {

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageServiceImplementation(ImageRepository imageRepository, ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ImageDto createImage(ImageDto imageDto) {
        Image image = modelMapper.map(imageDto, Image.class);
        Image savedImage = imageRepository.save(image);
        return modelMapper.map(savedImage, ImageDto.class);
    }

    @Override
    public ImageDto getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", String.valueOf(id)));
        return modelMapper.map(image, ImageDto.class);
    }

    @Override
    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageDto updateImage(Long id, ImageDto imageDto) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", String.valueOf(id)));
        image.setUrl(imageDto.getUrl());
        Image updatedImage = imageRepository.save(image);
        return modelMapper.map(updatedImage, ImageDto.class);
    }

    @Override
    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", String.valueOf(id)));
        imageRepository.delete(image);
    }
}
