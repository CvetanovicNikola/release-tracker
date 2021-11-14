package com.neon.releasetracker.exceptions;

public class MissingReleaseParameterException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public MissingReleaseParameterException(String message) {
		super(message);
	}
}
