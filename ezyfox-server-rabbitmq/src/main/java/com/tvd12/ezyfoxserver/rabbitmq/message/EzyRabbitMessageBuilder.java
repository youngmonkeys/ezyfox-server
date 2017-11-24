package com.tvd12.ezyfoxserver.rabbitmq.message;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzyRabbitMessageBuilder implements EzyBuilder<EzyRabbitMessage> {

	protected Object body = null;
	protected String exchange = "exchange";
	protected boolean mandatory = false;
	protected boolean immediate = false;
	protected String routingKey = "routingKey";
	protected BasicProperties properties = null;
	
	public static EzyRabbitMessageBuilder messageBuilder() {
		return new EzyRabbitMessageBuilder();
	}
	
	public EzyRabbitMessageBuilder body(Object body) {
		this.body = body;
		return this;
	}
	
	public EzyRabbitMessageBuilder exchange(String exchange) {
		this.exchange = exchange;
		return this;
	}
	
	public EzyRabbitMessageBuilder mandatory(boolean mandatory) {
		this.mandatory = mandatory;
		return this;
	}
	
	public EzyRabbitMessageBuilder immediate(boolean immediate) {
		this.immediate = immediate;
		return this;
	}
	
	public EzyRabbitMessageBuilder routingKey(String routingKey) {
		this.routingKey = routingKey;
		return this;
	}
	
	public EzyRabbitMessageBuilder properties(BasicProperties properties) {
		this.properties = properties;
		return this;
	}
	
	@Override
	public EzyRabbitMessage build() {
		EzyRabbitSimpleMessage message = new EzyRabbitSimpleMessage();
		message.setBody(body);
		message.setExchange(exchange);
		message.setMandatory(mandatory);
		message.setImmediate(immediate);
		message.setRoutingKey(routingKey);
		message.setProperties(properties);
		return message;
	}
	
}
