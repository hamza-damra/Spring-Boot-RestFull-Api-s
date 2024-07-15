package com.hamza.spring.myblog.validation.validators;

import com.hamza.spring.myblog.repository.PostRepository;
import com.hamza.spring.myblog.validation.annotations.UniqueTitle;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    @Autowired
    private PostRepository postRepository;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null) {
            return true;
        }
        return !postRepository.existsByTitle(title);
    }
}
