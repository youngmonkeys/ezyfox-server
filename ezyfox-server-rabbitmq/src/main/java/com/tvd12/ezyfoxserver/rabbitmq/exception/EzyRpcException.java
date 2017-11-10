package com.tvd12.ezyfoxserver.rabbitmq.exception;

import lombok.Getter;

public class EzyRpcException extends Exception {
	private static final long serialVersionUID = 6116417224071566449L;
	
	@Getter
	private int code;
	
	public EzyRpcException(int code, String message) {
		super(message);
		this.code = code;
	}

}
