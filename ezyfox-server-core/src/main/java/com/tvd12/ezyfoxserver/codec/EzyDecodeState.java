package com.tvd12.ezyfoxserver.codec;

import lombok.Getter;

public enum EzyDecodeState implements EzyIDecodeState {

	PREPARE_MESSAGE(0),
	READ_MESSAGE_HEADER(1),
	READ_MESSAGE_SIZE(2),
	READ_MESSAGE_CONTENT(3);
	
	@Getter
	private final int id;
	
	private EzyDecodeState(final int id) {
		this.id = id;
	}
	
}
