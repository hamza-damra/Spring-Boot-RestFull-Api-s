package com.hamza.spring.myblog.validation.annotations;

import com.hamza.spring.myblog.validation.validators.UniqueTitleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueTitleValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTitle {
    String message() default "Title must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
