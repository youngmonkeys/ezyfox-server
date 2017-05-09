package com.tvd12.ezyfoxserver.exception;

public class EzyMaxRequestSizeException extends IllegalStateException {
	private static final long serialVersionUID = -3982995135416662086L;

	public EzyMaxRequestSizeException(String msg) {
		super(msg);
	}
	
	public EzyMaxRequestSizeException(int size, int maxSize) {
		this("size = " + size + " when max size = " + maxSize);
	}
	
}
