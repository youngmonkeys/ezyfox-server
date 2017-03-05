package com.tvd12.ezyfoxserver.function;

public interface CastIntToByte {

	default byte cast(int value) {
		return (byte)value;
	}

	
}
