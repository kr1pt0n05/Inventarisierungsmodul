package com.hs_esslingen.insy.exception;

/**
 * Custom exception class to handle not found errors.
 * This exception is thrown when a requested resource is not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}