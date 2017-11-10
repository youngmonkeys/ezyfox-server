package com.tvd12.ezyfoxserver.exception;

public class EzyRequestHandleException extends IllegalStateException {
	private static final long serialVersionUID = 6288790755559864267L;
	
	public EzyRequestHandleException(String msg, Throwable e) {
		super(msg, e);
	}

}
