package com.tvd12.ezyfoxserver.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzyRabbitServerConfigBuilder 
		implements EzyBuilder<EzyRabbitServerConfig> {

	protected String queue = "queue";
	protected boolean autoAck = true;
	protected boolean noLocal = false;
	protected boolean exclusive = false;
	protected String consumerTag = "";
	protected Map<String, Object> arguments = new HashMap<>();
	
	public static EzyRabbitServerConfigBuilder serverConfigBuilder() {
		return new EzyRabbitServerConfigBuilder();
	}
	
	public EzyRabbitServerConfigBuilder queue(String queue) {
		this.queue = queue;
		return this;
	}
	
	public EzyRabbitServerConfigBuilder autoAck(boolean autoAck) {
		this.autoAck = autoAck;
		return this;
	}
	
	public EzyRabbitServerConfigBuilder noLocal(boolean noLocal) {
		this.noLocal = noLocal;
		return this;
	}
	
	public EzyRabbitServerConfigBuilder exclusive(boolean exclusive) {
		this.exclusive = exclusive;
		return this;
	}
	
	public EzyRabbitServerConfigBuilder consumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
		return this;
	}
	
	public EzyRabbitServerConfigBuilder arguments(Map<String, Object> arguments) {
		this.arguments.putAll(arguments);
		return this;
	}
	
	@Override
	public EzyRabbitServerConfig build() {
		EzyRabbitSimpleServerConfig config = new EzyRabbitSimpleServerConfig();
		config.setQueue(queue);
		config.setAutoAck(autoAck);
		config.setNoLocal(noLocal);
		config.setExclusive(exclusive);
		config.setArguments(arguments);
		config.setConsumerTag(consumerTag);
		return config;
	}
}
