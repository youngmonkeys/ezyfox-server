package com.tvd12.ezyfoxserver.exception;

import java.io.File;

public class EzyFileNotFoundException extends IllegalArgumentException {
	private static final long serialVersionUID = 8543271843235809091L;
	
	public EzyFileNotFoundException(Throwable e) {
		super(e);
	}
	
	public EzyFileNotFoundException(String msg) {
		super(msg);
	}
	
	public EzyFileNotFoundException(File file) {
		super(file.getAbsolutePath() + " not found!");
	}
	
	public EzyFileNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

}
