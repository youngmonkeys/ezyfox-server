package com.tvd12.ezyfoxserver.codec;

public class EzyMessageBuilder {

	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	
	public static EzyMessageBuilder newInstance() {
		return new EzyMessageBuilder();
	}
	
	public EzyMessageBuilder size(int size) {
		this.size = size;
		return this;
	}
	
	public EzyMessageBuilder content(byte[] content) {
		this.content = content;
		return this;
	}
	
	public EzyMessageBuilder header(EzyMessageHeader header) {
		this.header = header;
		return this;
	}
	
	public EzyMessageBuilder header(EzyMessageHeaderBuilder buider) {
		return header(buider.build());
	}
	
	public EzyMessage build() {
		EzySimpleMessage answer = new EzySimpleMessage();
		answer.setSize(size);
		answer.setHeader(header);
		answer.setContent(content);
		answer.countBytes();
		return answer;
	}
	
}