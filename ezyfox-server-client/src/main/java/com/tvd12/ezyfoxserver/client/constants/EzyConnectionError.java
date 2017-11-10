package com.tvd12.ezyfoxserver.client.constants;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;

public enum EzyConnectionError implements EzyConstant {

	UNKNOWN(0),
	NETWORK_UNREACHABLE(1),
	CONNECTION_REFUSED(2),
	NO_ROUTE_TO_HOST(2);
	
	@Getter
	private int id;
	
	private EzyConnectionError(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
}
