package com.tvd12.ezyfoxserver.kafka.builder.impl;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaProducerBuilder;

@SuppressWarnings("rawtypes")
public class EzyKafkaSimpleProducerBuilder
		extends EzyKafkaSimpleEndpointBuilder<EzyKafkaProducerBuilder>
		implements EzyKafkaProducerBuilder {
	
	@Override
	public Producer build() {
		return new KafkaProducer<>(properties);
	}
	
}
