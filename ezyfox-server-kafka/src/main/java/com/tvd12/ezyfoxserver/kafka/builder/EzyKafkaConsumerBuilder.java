package com.tvd12.ezyfoxserver.kafka.builder;

import org.apache.kafka.clients.consumer.Consumer;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.kafka.builder.impl.EzyKafkaSimpleConsumerBuilder;

@SuppressWarnings("rawtypes")
public interface EzyKafkaConsumerBuilder
		extends EzyKafkaEndpointBuilder<EzyKafkaConsumerBuilder>, EzyBuilder<Consumer> {
	
	static EzyKafkaConsumerBuilder consumerBuilder() {
		return new EzyKafkaSimpleConsumerBuilder();
	}
	
}
