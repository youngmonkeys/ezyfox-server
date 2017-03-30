package com.tvd12.ezyfoxserver.codec;

public class EzyMessageBuilder {

	// prevent new instance
	private EzyMessageBuilder() {
	}
	
	public static MessageBuilder messageBuilder() {
		return new MessageBuilder();
	}
	
	public static MessageHeaderBuilder headerBuilder() {
		return new MessageHeaderBuilder();
	}
	
}

class MessageHeaderBuilder {
	
	private boolean bigSize;
	private boolean encrypted;
	private boolean compressed;
	
	public MessageHeaderBuilder bigSize(boolean bigSize) {
		this.bigSize = bigSize;
		return this;
	}
	public MessageHeaderBuilder encrypted(boolean encrypted) {
		this.encrypted = encrypted;
		return this;
	}
	public MessageHeaderBuilder compressed(boolean compressed) {
		this.compressed = compressed;
		return this;
	}
	
	public EzyMessageHeader build() {
		EzySimpleMessageHeader header = new EzySimpleMessageHeader();
		header.setBigSize(bigSize);
		header.setEncrypted(encrypted);
		header.setCompressed(compressed);
		return header;
	}
}

class MessageBuilder {
	
	private int size;
	private byte[] content;
	private EzyMessageHeader header;
	
	public MessageBuilder size(int size) {
		this.size = size;
		return this;
	}
	
	public MessageBuilder content(byte[] content) {
		this.content = content;
		return this;
	}
	
	public MessageBuilder header(EzyMessageHeader header) {
		this.header = header;
		return this;
	}
	
	public MessageBuilder header(MessageHeaderBuilder buider) {
		this.header = buider.build();
		return this;
	}
	
	public EzyMessage build() {
		EzySimpleMessage answer = new EzySimpleMessage();
		answer.setSize(size);
		answer.setHeader(header);
		answer.setContent(content);
		return answer;
	}
	
}
