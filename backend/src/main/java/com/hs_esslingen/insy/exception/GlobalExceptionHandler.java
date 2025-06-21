package com.hs_esslingen.insy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hs_esslingen.insy.dto.ErrorResponseDTO;

/**
 * Global exception handler for handling various exceptions in the application.
 * This class intercepts exceptions thrown by controllers and returns
 * appropriate
 * HTTP responses with error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO NotFound(Exception e) {
        return new ErrorResponseDTO(404, e.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO InternalServerError(Exception e) {
        return new ErrorResponseDTO(500, e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO BadRequest(Exception e) {
        return new ErrorResponseDTO(400, e.getMessage());
    }
}