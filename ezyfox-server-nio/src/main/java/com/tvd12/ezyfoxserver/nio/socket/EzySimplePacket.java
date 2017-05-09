package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimplePacket implements EzyPacket {

	private Object data;
	private EzyTransportType type = EzyTransportType.TCP;

}
