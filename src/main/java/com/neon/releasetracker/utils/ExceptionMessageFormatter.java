package com.neon.releasetracker.utils;

public class ExceptionMessageFormatter {
	
	public static String releaseNotFoundByName(String name) {
		return String.format("There is no release with name %s", name);
	}
	
	public static String releaseNotFoundById(Long id) {
		return String.format("There is no release with id %s", id);
	}

	public static String releaseNotFoundByStatus(String status) {
		return String.format("There is no release with status %s", status);
	}
	
	public static String releaseAlreadyExists(String param) {
		return String.format("Release with name %s already exists", param);
	}
	
	public static String missingReleaseParameter(String param) {
		return String.format("Missing %s for release", param);
	}

}
