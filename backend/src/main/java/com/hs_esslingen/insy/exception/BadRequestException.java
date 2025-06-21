package com.hs_esslingen.insy.exception;

/**
 * Custom exception class to handle bad request errors.
 * This exception is thrown when the request made by the client is invalid or
 * malformed.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
