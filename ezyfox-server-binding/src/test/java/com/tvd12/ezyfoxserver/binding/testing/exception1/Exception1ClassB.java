package com.tvd12.ezyfoxserver.binding.testing.exception1;

import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;

@EzyObjectBinding
public class Exception1ClassB {

	public String setValue(String value) {
		throw new IllegalArgumentException();
	}
	
}
