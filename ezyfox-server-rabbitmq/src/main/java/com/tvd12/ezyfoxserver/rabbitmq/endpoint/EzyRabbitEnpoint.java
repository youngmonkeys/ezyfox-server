package com.tvd12.ezyfoxserver.rabbitmq.endpoint;

import com.rabbitmq.client.Channel;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import lombok.Setter;

@Setter
public abstract class EzyRabbitEnpoint 
		extends EzyLoggable 
		implements EzyStartable {

	protected String queue;
	protected Channel channel;
	protected EzyMessageSerializer messageSerializer;
	protected EzyMessageDeserializer messageDeserializer;
	
	protected byte[] serializeToBytes(Object value) {
		return messageSerializer.serialize(value);
	}
	
	protected <T> T deserializeToObject(byte[] value) {
		return messageDeserializer.deserialize(value);
	}
	
	public static abstract class Builder<B extends Builder<B>> 
			implements EzyBuilder<EzyRabbitEnpoint> {
		protected String queue;
		protected Channel channel;
		protected EzyMessageSerializer messageSerializer;
		protected EzyMessageDeserializer messageDeserializer;
		
		public B queue(String queue) {
			this.queue = queue;
			return getThis();
		}
		
		public B channel(Channel channel) {
			this.channel = channel;
			return getThis();
		}
		
		public B messageSerializer(EzyMessageSerializer messageSerializer) {
			this.messageSerializer = messageSerializer;
			return getThis();
		}
		
		public B messageDeserializer(EzyMessageDeserializer messageDeserializer) {
			this.messageDeserializer = messageDeserializer;
			return getThis();
		}
		
		@Override
		public EzyRabbitEnpoint build() {
			EzyRabbitEnpoint answer = newProduct();
			answer.setQueue(queue);
			answer.setChannel(channel);
			answer.setMessageSerializer(messageSerializer);
			answer.setMessageDeserializer(messageDeserializer);
			return answer;
		}
		
		protected abstract EzyRabbitEnpoint newProduct();
		
		@SuppressWarnings("unchecked")
		protected B getThis() {
			return (B)this;
		}
	}
	
}
