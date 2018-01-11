package com.tvd12.ezyfoxserver.codec;

public class EzyMessageHeaderBuilder {
	
	protected boolean bigSize;
	protected boolean encrypted;
	protected boolean compressed;
	protected boolean text;
	
	public static EzyMessageHeaderBuilder newInstance() {
		return new EzyMessageHeaderBuilder();
	}
	
	public static EzyMessageHeaderBuilder messageHeaderBuilder() {
		return new EzyMessageHeaderBuilder();
	}
	
	public EzyMessageHeaderBuilder bigSize(boolean bigSize) {
		this.bigSize = bigSize;
		return this;
	}
	
	public EzyMessageHeaderBuilder encrypted(boolean encrypted) {
		this.encrypted = encrypted;
		return this;
	}
	
	public EzyMessageHeaderBuilder compressed(boolean compressed) {
		this.compressed = compressed;
		return this;
	}
	
	public EzyMessageHeaderBuilder text(boolean text) {
		this.text = text;
		return this;
	}
	
	public EzyMessageHeader build() {
		EzySimpleMessageHeader header = new EzySimpleMessageHeader();
		header.setBigSize(bigSize);
		header.setEncrypted(encrypted);
		header.setCompressed(compressed);
		header.setText(text);
		return header;
	}
}