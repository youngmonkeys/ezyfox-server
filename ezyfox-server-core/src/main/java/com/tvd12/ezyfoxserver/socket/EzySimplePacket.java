package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimplePacket implements EzyPacket {

    @Setter
	private Object data;
	private boolean released;
	private boolean fragmented;
	@Setter
	private EzyTransportType type = EzyTransportType.TCP;
	
	@Override
	public void setFragment(Object fragment) {
	    this.data = fragment;
	    this.fragmented = true;
	}

	@Override
	public void release() {
	    this.data = null;
	    this.released = true;
	}
	
	@Override
	public String toString() {
	    return new StringBuilder()
	            .append("(")
	            .append("transportType: ").append(type)
	            .append(", data: ").append(data)
	            .append(")")
	            .toString();
	}
}
