package com.tvd12.ezyfoxserver.codec;

import lombok.Getter;

public enum EzyDecodeState implements EzyIDecodeState {

	PREPARE_MESSAGE(99),
	READ_MESSAGE_SIZE(100),
	READ_MESSAGE_CONTENT(101);
	
	@Getter
	private final int id;
	
	private EzyDecodeState(final int id) {
		this.id = id;
	}
	
}
