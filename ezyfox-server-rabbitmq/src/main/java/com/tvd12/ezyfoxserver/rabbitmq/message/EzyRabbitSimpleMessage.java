package com.tvd12.ezyfoxserver.rabbitmq.message;

import java.io.Serializable;

import com.rabbitmq.client.AMQP.BasicProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyRabbitSimpleMessage 
		implements EzyRabbitMessage, Serializable {
	private static final long serialVersionUID = -511730695910933117L;
	
	protected Object body;
	protected String exchange;
	protected boolean mandatory;
	protected boolean immediate;
	protected String routingKey;
	protected BasicProperties properties;
	
}
