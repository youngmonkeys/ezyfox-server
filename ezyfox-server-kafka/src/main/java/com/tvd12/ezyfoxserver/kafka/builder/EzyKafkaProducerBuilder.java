package com.tvd12.ezyfoxserver.kafka.builder;

import org.apache.kafka.clients.producer.Producer;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.kafka.builder.impl.EzyKafkaSimpleProducerBuilder;

@SuppressWarnings("rawtypes")
public interface EzyKafkaProducerBuilder 
		extends EzyKafkaEndpointBuilder<EzyKafkaProducerBuilder>, EzyBuilder<Producer> {
	
	static EzyKafkaProducerBuilder producerBuilder() {
		return new EzyKafkaSimpleProducerBuilder();
	}
	
}
