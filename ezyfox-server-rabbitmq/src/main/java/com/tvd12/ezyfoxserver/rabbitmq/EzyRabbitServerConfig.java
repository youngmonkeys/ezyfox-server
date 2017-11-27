package com.tvd12.ezyfoxserver.rabbitmq;

import java.util.Map;

public interface EzyRabbitServerConfig {

	String getQueue();
	
	boolean isAutoAck();
	
	boolean isNoLocal();
	
	boolean isExclusive();
	
	String getConsumerTag();
	
	Map<String, Object> getArguments();
	
}
