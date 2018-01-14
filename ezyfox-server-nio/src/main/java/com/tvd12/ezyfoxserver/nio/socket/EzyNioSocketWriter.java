package com.tvd12.ezyfoxserver.nio.socket;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.socket.EzySocketWriter;

public class EzyNioSocketWriter extends EzySocketWriter {

	protected ByteBuffer writeBuffer = ByteBuffer.allocate(32768);
	
	@Override
	protected Object getWriteBuffer() {
		return writeBuffer;
	}
}
