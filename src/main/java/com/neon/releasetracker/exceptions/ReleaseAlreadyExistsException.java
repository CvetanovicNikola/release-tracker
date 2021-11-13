package com.neon.releasetracker.exceptions;

public class ReleaseAlreadyExistsException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ReleaseAlreadyExistsException(String message) {
		super(message);
	}
	
}
