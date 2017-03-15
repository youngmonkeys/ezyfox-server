package com.tvd12.ezyfoxserver.exception;

public class EzyResponseHandleException extends IllegalStateException {
	private static final long serialVersionUID = 6288790755559864267L;
	
	public EzyResponseHandleException(String msg, Throwable e) {
		super(msg, e);
	}

}
