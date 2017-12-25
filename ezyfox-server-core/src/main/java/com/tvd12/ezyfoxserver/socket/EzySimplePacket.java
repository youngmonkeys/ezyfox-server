package com.tvd12.ezyfoxserver.socket;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimplePacket implements EzyPacket {

    @Setter
	private Object data;
	private boolean released;
	private boolean fragmented;
	private ByteBuffer fragment;
	@Setter
	private EzyTransportType type = EzyTransportType.TCP;
	
	public void setFragment(ByteBuffer fragment) {
	    this.fragment = fragment;
	    this.fragmented = true;
	}

	@Override
	public void release() {
	    this.data = null;
	    this.fragment = null;
	    this.released = true;
	}
	
	@Override
	public String toString() {
	    return new StringBuilder()
	            .append("transportType: ").append(type)
	            .append(", data: ").append(data)
	            .append(", fragment: ").append(fragment)
	            .toString();
	}
}
