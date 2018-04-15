package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;

public interface EzyObjectSerializer {

	/**
	 * serialize a value to byte array
	 * 
	 * @param value the value
	 * @return the byte array
	 */
	byte[] serialize(Object value);
	
	/**
	 * serialize a value to byte buffer
	 * 
	 * @param value the value 
	 * @return the byte buffer
	 */
	default ByteBuffer write(Object value) {
		
		// serialize value to byte array
		byte[] bytes = serialize(value);
		
		// wrap the byte array
		return ByteBuffer.wrap(bytes);
	}
	
}
