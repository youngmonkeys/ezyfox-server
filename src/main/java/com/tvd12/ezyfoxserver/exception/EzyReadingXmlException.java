package com.tvd12.ezyfoxserver.exception;

public class EzyReadingXmlException extends IllegalStateException {
	private static final long serialVersionUID = 3571161488931185555L;
	
	public EzyReadingXmlException() {
	}
	
	public EzyReadingXmlException(final String msg) {
		super(msg);
	}
	
	public EzyReadingXmlException(final Throwable e) {
		super(e);
	}
	
	public EzyReadingXmlException(final String msg, final Throwable e) {
		super(msg, e);
	}

}
