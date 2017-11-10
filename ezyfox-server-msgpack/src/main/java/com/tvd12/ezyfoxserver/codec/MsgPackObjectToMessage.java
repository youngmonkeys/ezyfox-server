package com.tvd12.ezyfoxserver.codec;

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
		return EzyMessageBuilder.newInstance()
				.size(content.length)
				.content(content)
				.header(newHeader(content))
				.build();
	}
	
	private EzyMessageHeader newHeader(byte[] content) {
		return EzyMessageHeaderBuilder.newInstance()
				.bigSize(isBigMessage(content))
				.encrypted(false)
				.compressed(false)
				.build();
	}
	
	private boolean isBigMessage(byte[] content) {
		return content.length > MsgPackConstant.MAX_SMALL_MESSAGE_SIZE;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {

		public EzyObjectToMessage build() {
			return new MsgPackObjectToMessage(this);
		}
		
		protected EzyObjectToBytes newObjectToBytes() {
			return MsgPackObjectToBytes.builder().serializer(newSerializer()).build();
		}
		
		protected EzyMessageSerializer newSerializer() {
			return new MsgPackSimpleSerializer();
		}
	}
	
}
