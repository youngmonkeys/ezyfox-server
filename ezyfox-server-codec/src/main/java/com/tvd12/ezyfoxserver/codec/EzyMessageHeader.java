package com.tvd12.ezyfoxserver.codec;

public interface EzyMessageHeader {

	/**
	 * Check whether message use 4 bytes for message size or 2 bytes
	 * 
	 * @return true or false
	 */
	boolean isBigSize(); //bit 1
	
	/**
	 * Check whether message is encrypted or not
	 * 
	 * @return true or false
	 */
	boolean isEncrypted(); // bit 2
	
	/**
	 * Check whether message is compressed or not
	 * 
	 * @return true or false
	 */
	boolean isCompressed(); // bit 3
	
	/**
	 * Check whether message is text or binary type
	 * 
	 * @return true is text or false is binary
	 */
	boolean isText(); // bit 4
	
}
