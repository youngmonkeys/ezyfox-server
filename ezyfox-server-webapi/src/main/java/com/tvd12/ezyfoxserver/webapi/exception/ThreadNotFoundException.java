package com.tvd12.ezyfoxserver.webapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ThreadNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -1866279274715530482L;
	
	public ThreadNotFoundException(String message) {
		super(message);
	}
	
	public static ThreadNotFoundException notFound(long threadId) {
		return new ThreadNotFoundException("thread: " + threadId + " is not found");
	}
	
	public static ThreadNotFoundException invalid(long threadId) {
		return new ThreadNotFoundException("thread: " + threadId + " is invalid");
	}

}
