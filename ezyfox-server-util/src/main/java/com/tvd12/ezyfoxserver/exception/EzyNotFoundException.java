package com.tvd12.ezyfoxserver.exception;

public class EzyNotFoundException extends IllegalArgumentException {
	private static final long serialVersionUID = 4266477686637543686L;
	
	public EzyNotFoundException() {
	}
	
	public EzyNotFoundException(String message) {
		super(message);
	}
	
	public EzyNotFoundException(Throwable throwable) {
		super(throwable);
	}
	
	public EzyNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
