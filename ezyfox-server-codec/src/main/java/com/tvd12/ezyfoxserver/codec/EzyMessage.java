package com.tvd12.ezyfoxserver.codec;

public interface EzyMessage {

	/**
	 * Get size of message
	 * 
	 * @return the size of message
	 */
	int getSize();
	
	/**
	 * Get content of message in byte array
	 * 
	 * @return the content of message
	 */
	byte[] getContent();
	
	/**
	 * Get header of message
	 * 
	 * @return the message header
	 */
	EzyMessageHeader getHeader();
	
	/**
	 * Get length of message's size, 2 or 4
	 * 
	 * @return the length of message's size
	 */
	default int getSizeLength() {
		return getHeader().isBigSize() ? 4 : 2;
	}
	
	default boolean hasBigSize() {
		return getHeader().isBigSize();
	}
	
}
