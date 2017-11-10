package com.tvd12.ezyfoxserver.function;

public interface EzyCastIntToByte {

	default byte cast(int value) {
		return (byte)value;
	}
	
	default byte cast(long value) {
		return (byte)value;
	}

	
}
