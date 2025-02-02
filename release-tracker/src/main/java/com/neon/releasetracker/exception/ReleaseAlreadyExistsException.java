package com.neon.releasetracker.exception;

public class ReleaseAlreadyExistsException extends RuntimeException{
    public ReleaseAlreadyExistsException(String message) {
        super(message);
    }
}
