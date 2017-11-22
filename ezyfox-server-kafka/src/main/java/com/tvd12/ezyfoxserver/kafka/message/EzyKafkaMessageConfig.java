package com.tvd12.ezyfoxserver.kafka.message;

public interface EzyKafkaMessageConfig {

	Class<?> getKeyType();
	
	Class<?> getValueType();
	
}
