package com.hamza.spring.myblog.exception;

import com.hamza.spring.myblog.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BlogApiException.class)
    protected ResponseEntity<ErrorDetails> handleBlogApiException(BlogApiException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDetails> handleGlobalApiException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
