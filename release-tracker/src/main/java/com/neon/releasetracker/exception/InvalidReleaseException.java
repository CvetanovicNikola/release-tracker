package com.neon.releasetracker.exception;

public class InvalidReleaseException extends RuntimeException {
    public InvalidReleaseException(String message) {
        super(message);
    }
}
