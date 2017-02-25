package com.tvd12.ezyfoxserver.codec;

import org.msgpack.MessagePack;

public class MsgPackObjectToMessage implements EzyObjectToMessage {

	private EzyObjectToBytes objectToBytes;
	
	protected MsgPackObjectToMessage(Builder builder) {
		this.objectToBytes = builder.newObjectToBytes();
	}
	
	@Override
	public EzyMessage convert(Object object) {
		return convert(convertObject(object));
	}
	
	private byte[] convertObject(Object object) {
		return objectToBytes.convert(object);
	}
	
	private EzyMessage convert(byte[] content) {
		EzyMessage message = new EzyMessage();
		message.setSize(content.length);
		message.setContent(content);
		return message;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private MessagePack msgPack;
		
		public Builder msgPack(MessagePack msgPack) {
			this.msgPack = msgPack;
			return this;
		}
		
		public EzyObjectToMessage build() {
			return new MsgPackObjectToMessage(this);
		}
		
		protected EzyObjectToBytes newObjectToBytes() {
			return MsgPackObjectToBytes.builder().msgPack(msgPack).build();
		}
	}
	
}
