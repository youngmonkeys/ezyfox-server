package com.tvd12.ezyfoxserver.rabbitmq.endpoint;

import com.rabbitmq.client.Channel;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public abstract class EzyRabbitEndpoint 
		extends EzyLoggable 
		implements EzyStartable {

	protected String queue;
	protected Channel channel;
	protected EzyMessageSerializer messageSerializer;
	protected EzyMessageDeserializer messageDeserializer;
	
	protected EzyRabbitEndpoint(Builder<?> builder) {
        this.queue = builder.queue;
        this.channel = builder.channel;
        this.messageSerializer = builder.messageSerializer;
        this.messageDeserializer = builder.messageDeserializer;
	}
	
	protected byte[] serializeToBytes(Object value) {
		return messageSerializer.serialize(value);
	}
	
	protected <T> T deserializeToObject(byte[] value) {
		return messageDeserializer.deserialize(value);
	}
	
	@SuppressWarnings("unchecked")
	public static abstract class Builder<B extends Builder<B>> 
			implements EzyBuilder<EzyRabbitEndpoint> {
		protected String queue;
		protected Channel channel;
		protected EzyMessageSerializer messageSerializer;
		protected EzyMessageDeserializer messageDeserializer;
		
		public B queue(String queue) {
			this.queue = queue;
			return (B)this;
		}
		
		public B channel(Channel channel) {
			this.channel = channel;
			return (B)this;
		}
		
		public B messageSerializer(EzyMessageSerializer messageSerializer) {
			this.messageSerializer = messageSerializer;
			return (B)this;
		}
		
		public B messageDeserializer(EzyMessageDeserializer messageDeserializer) {
			this.messageDeserializer = messageDeserializer;
			return (B)this;
		}
		
	}
	
}
