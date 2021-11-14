package com.neon.releasetracker.enums;

import java.util.Arrays;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	CREATED("created"),
	IN_DEVELOPMENT("in_development"),
	ON_DEV("on_dev"),
	QA_DONE_ON_DEV("qa_done_on_dev"),
	ON_STAGING("on_staging"),
	QA_DONE_ON_STAGING("qa_done_on_staging"),
	ON_PROD("on_prod"),
	DONE("done"); 
	
	private final String releaseStatus;
	

	
	private Status(String releaseStatus) {
		this.releaseStatus = releaseStatus;
	
	}
	
	public String getReleaseStatus() {
		return releaseStatus;
	}
	
    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(releaseStatus);
    }
    

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromString(String value) {
    	
    return Arrays.stream(Status.values())
            .filter(status -> status.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> {
                throw new IllegalArgumentException("Not valid status for a release");
            });
    }
}