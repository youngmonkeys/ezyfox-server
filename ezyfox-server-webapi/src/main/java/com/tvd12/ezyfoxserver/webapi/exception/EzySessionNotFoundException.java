package com.tvd12.ezyfoxserver.webapi.exception;

public class EzySessionNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -1866279274715530482L;
	
	public EzySessionNotFoundException(String message) {
		super(message);
	}
	
	public static EzySessionNotFoundException notFound(long sessionId) {
		return new EzySessionNotFoundException("session with id: " + sessionId + " is not found or not activated");
	}

}
