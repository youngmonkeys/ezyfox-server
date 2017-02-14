package com.tvd12.ezyfoxserver.exception;

public class EzyFoxReadingXmlException extends IllegalStateException {
	private static final long serialVersionUID = 3571161488931185555L;
	
	public EzyFoxReadingXmlException() {
	}
	
	public EzyFoxReadingXmlException(final String msg) {
		super(msg);
	}
	
	public EzyFoxReadingXmlException(final Throwable e) {
		super(e);
	}
	
	public EzyFoxReadingXmlException(final String msg, final Throwable e) {
		super(msg, e);
	}

}
