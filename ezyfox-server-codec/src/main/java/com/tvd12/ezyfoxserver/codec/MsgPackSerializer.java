package com.tvd12.ezyfoxserver.codec;

public interface MsgPackSerializer {

	byte[] serialize(Object value);
	
}
