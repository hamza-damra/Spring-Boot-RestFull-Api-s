package com.hamza.spring.myblog.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BlogApiException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorMessage;
    private String errorMessageDetails;

    public BlogApiException(HttpStatus httpStatus, String errorMessage, String errorMessageDetails) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorMessageDetails = errorMessageDetails;
    }

    public BlogApiException(HttpStatus httpStatus, String errorMessage) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public BlogApiException(String errorMessage) {
        super(errorMessage);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorMessage = errorMessage;
    }
}
