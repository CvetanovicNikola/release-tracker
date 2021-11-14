package com.neon.releasetracker.enumConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.neon.releasetracker.enums.Status;
@Component
public class StatusEnumConverter implements Converter<String, Status>{

	@Override
	public Status convert(String statusName) {
		
		return Status.valueOf(statusName.toUpperCase()); 
	}

}
