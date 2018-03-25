package com.tvd12.ezyfoxserver.webapi.exception;

public class EzyUserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -1866279274715530482L;
	
	public EzyUserNotFoundException(String message) {
		super(message);
	}
	
	public static EzyUserNotFoundException notFound(String username) {
		return new EzyUserNotFoundException("user: " + username + " is not found");
	}
	
	public static EzyUserNotFoundException invalidPassword(String username) {
		return new EzyUserNotFoundException("user: " + username + " is found, but invalid password");
	}

}
