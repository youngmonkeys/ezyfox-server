package com.tvd12.ezyfoxserver.codec;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyMessage {

	private int size;
	private byte[] content;
	
}
