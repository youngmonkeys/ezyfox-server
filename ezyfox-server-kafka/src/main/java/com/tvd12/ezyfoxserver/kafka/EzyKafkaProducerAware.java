package com.tvd12.ezyfoxserver.kafka;

import org.apache.kafka.clients.producer.Producer;

@SuppressWarnings("rawtypes")
public interface EzyKafkaProducerAware {

	void setProducer(Producer producer);
	
}
