package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;

public interface EzySender {

	void send(EzyData data, EzyTransportType type);
	
	default void send(EzyData data) {
		send(data, EzyTransportType.TCP);
	}
	
}
