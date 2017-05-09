package com.tvd12.ezyfoxserver.hazelcast.transaction;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyTransactionType implements EzyConstant {

	TWO_PHASE(1),
	ONE_PHASE(2);
	
	@Getter
	private int id;
	
	private EzyTransactionType(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyTransactionType valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
}
