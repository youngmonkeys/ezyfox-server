package com.tvd12.ezyfoxserver.kafka;

import org.apache.kafka.clients.consumer.Consumer;

@SuppressWarnings("rawtypes")
public interface EzyKafkaConsumerAware {

	void setConsumer(Consumer consumer);
	
}
