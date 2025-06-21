package com.hs_esslingen.insy.exception;

/**
 * Custom exception class to handle internal server errors.
 * This exception is thrown when an unexpected error occurs on the server side.
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
