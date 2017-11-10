package com.tvd12.ezyfoxserver.database.exception;

public class EzyTransactionException extends Exception {
	private static final long serialVersionUID = 2578127055193650532L;
	
	public EzyTransactionException() {
		super();
	}
	
	public EzyTransactionException(String msg) {
		super(msg);
	}
	
	public EzyTransactionException(String msg, Throwable e) {
		super(msg, e);
	}

}
