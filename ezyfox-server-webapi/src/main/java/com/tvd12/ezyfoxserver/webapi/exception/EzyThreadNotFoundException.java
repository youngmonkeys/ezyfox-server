package com.tvd12.ezyfoxserver.webapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EzyThreadNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -1866279274715530482L;
	
	public EzyThreadNotFoundException(String message) {
		super(message);
	}
	
	public static EzyThreadNotFoundException notFound(long threadId) {
		return new EzyThreadNotFoundException("thread: " + threadId + " is not found");
	}
	
	public static EzyThreadNotFoundException invalid(long threadId) {
		return new EzyThreadNotFoundException("thread: " + threadId + " is invalid");
	}

}
