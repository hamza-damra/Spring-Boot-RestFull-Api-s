package com.hamza.spring.myblog.validation;

import com.hamza.spring.myblog.repository.PostRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    @Autowired
    private PostRepository postRepository;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        return !postRepository.existsByTitle(title);
    }
}