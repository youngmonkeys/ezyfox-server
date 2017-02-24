package com.tvd12.ezyfoxserver.codec;

import lombok.Getter;

public enum DecodeState implements IDecodeState {

	PREPARE_MESSAGE(99),
	READ_MESSAGE_SIZE(100),
	READ_MESSAGE_CONTENT(101);
	
	@Getter
	private final int id;
	
	private DecodeState(final int id) {
		this.id = id;
	}
	
}
