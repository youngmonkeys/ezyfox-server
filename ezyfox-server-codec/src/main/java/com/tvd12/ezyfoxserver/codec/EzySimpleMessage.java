package com.tvd12.ezyfoxserver.codec;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EzySimpleMessage implements EzyMessage {

	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	
}
