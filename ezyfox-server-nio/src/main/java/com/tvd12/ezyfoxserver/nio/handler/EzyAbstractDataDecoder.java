package com.tvd12.ezyfoxserver.nio.handler;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyAbstractDataDecoder<D>
		extends EzyLoggable {

	protected ByteBuffer buffer;
	protected volatile boolean active;

	protected final D decoder;
	
	public EzyAbstractDataDecoder(D decoder) {
		this.active = true;
		this.decoder = decoder;
	}
	
	public void destroy() {
		this.active = false;
	}
	
}
