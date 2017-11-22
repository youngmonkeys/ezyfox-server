package com.tvd12.ezyfoxserver.kafka.record;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageConfig;

@SuppressWarnings("rawtypes")
public interface EzyKafkaConsumerRecordReader {

	EzyKafkaMessage read(ConsumerRecord record, EzyKafkaMessageConfig config);
	
}
