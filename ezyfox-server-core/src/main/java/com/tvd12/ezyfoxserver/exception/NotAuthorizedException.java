package com.tvd12.ezyfoxserver.exception;

public class NotAuthorizedException extends Exception {
	private static final long serialVersionUID = 8292545377354181644L;
	
	public NotAuthorizedException(String msg) {
		super(msg);
	}

}
