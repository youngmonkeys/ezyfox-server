package com.tvd12.ezyfoxserver.codec;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleMessage implements EzyMessage {

	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	
	@Setter(AccessLevel.NONE)
	private int byteCount;
	
	public void countBytes() {
		this.byteCount = 1 + getSizeLength() + getContent().length;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("(")
				.append("header: ")
					.append(header)
					.append(", ")
				.append("size: ")
					.append(size)
					.append(", ")
				.append("byteCount: ")
					.append(byteCount)
					.append(", ")
				.append("content: ")
					.append(Arrays.toString(content))
				.append(")")
				.toString();
	}
	
}
