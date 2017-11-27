package com.tvd12.ezyfoxserver.rabbitmq.message;

import com.rabbitmq.client.AMQP.BasicProperties;

public interface EzyRabbitMessage {

	Object getBody();
	
	String getExchange();
	
	boolean isMandatory();
	
	boolean isImmediate();
	
	String getRoutingKey();
	
	BasicProperties getProperties();
	
}
