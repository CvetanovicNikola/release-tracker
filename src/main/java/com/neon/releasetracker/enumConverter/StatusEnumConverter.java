package com.neon.releasetracker.enumConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.neon.releasetracker.enums.Status;

/**
 * Enum converter class so that we can use lowercase strings 
 * for statuses as url params.
 */
@Component
public class StatusEnumConverter implements Converter<String, Status>{

	@Override
	public Status convert(String statusName) {
		
		return Status.valueOf(statusName.toUpperCase()); 
	}

}
