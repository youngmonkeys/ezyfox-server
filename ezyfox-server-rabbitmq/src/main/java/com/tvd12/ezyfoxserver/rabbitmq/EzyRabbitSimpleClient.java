package com.tvd12.ezyfoxserver.rabbitmq;

import com.rabbitmq.client.Channel;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessage;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
public class EzyRabbitSimpleClient 
		extends EzyLoggable 
		implements EzyRabbitClient, EzyRabbitChannelAware {

	protected Channel channel;
	protected EzyMarshaller marshaller;
	protected EzyMessageSerializer messageSerializer;
	
	@Override
	public void shutdown() {
	}
	
	@Override
	public void send(EzyRabbitMessage message) {
		try {
			byte[] body = object2bytes(message.getBody());
			channel.basicPublish(
					message.getExchange(), 
					message.getRoutingKey(), 
					message.isMandatory(), 
					message.isImmediate(), 
					message.getProperties(),
					body
			);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't send message: " + message, e);
		}
	}
	
	protected byte[] object2bytes(Object object) {
		Object value = marshaller.marshal(object);
		byte[] bytes = messageSerializer.serialize(value);
		return bytes;
	}
	
}
