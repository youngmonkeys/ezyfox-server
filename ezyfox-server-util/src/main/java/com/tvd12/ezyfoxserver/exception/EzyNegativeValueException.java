package com.tvd12.ezyfoxserver.exception;

public class EzyNegativeValueException extends IllegalArgumentException {
	private static final long serialVersionUID = 3934085408440316839L;
	
	public EzyNegativeValueException(Number value) {
		super("negative value: " + value + " is not acceptable");
	}
}
