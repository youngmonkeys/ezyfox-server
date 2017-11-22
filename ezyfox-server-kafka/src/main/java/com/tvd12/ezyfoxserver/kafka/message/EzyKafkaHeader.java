package com.tvd12.ezyfoxserver.kafka.message;

public interface EzyKafkaHeader {

	String getKey();
	
	<T> T getValue();
	
}
