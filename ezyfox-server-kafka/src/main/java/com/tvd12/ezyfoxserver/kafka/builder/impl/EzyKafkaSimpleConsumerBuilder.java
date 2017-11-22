package com.tvd12.ezyfoxserver.kafka.builder.impl;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaConsumerBuilder;

@SuppressWarnings("rawtypes")
public class EzyKafkaSimpleConsumerBuilder
		extends EzyKafkaSimpleEndpointBuilder<EzyKafkaConsumerBuilder> 
		implements EzyKafkaConsumerBuilder {
	
	@Override
	public Consumer build() {
		return new KafkaConsumer<>(properties);
	}
	
}
