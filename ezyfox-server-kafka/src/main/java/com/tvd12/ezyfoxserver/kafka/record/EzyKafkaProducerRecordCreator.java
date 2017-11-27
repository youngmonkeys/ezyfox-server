package com.tvd12.ezyfoxserver.kafka.record;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;

@SuppressWarnings("rawtypes")
public interface EzyKafkaProducerRecordCreator {

	ProducerRecord create(EzyKafkaMessage message);
	
}
