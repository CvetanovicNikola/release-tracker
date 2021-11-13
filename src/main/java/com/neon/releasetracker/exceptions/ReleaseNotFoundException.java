package com.neon.releasetracker.exceptions;

public class ReleaseNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ReleaseNotFoundException(String message) {
		super(message);
	}
	
}
