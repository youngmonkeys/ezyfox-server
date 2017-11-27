package com.tvd12.ezyfoxserver.kafka.record;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshallerAware;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageBuilder;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageConfig;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
@SuppressWarnings("rawtypes")
public class EzyKafkaSimpleConsumerRecordReader
		extends EzyLoggable 
		implements EzyKafkaConsumerRecordReader, EzyUnmarshallerAware {

	protected EzyUnmarshaller unmarshaller;
	
	@Override
	public EzyKafkaMessage read(ConsumerRecord record, EzyKafkaMessageConfig config) {
		Object key = unmarshalObject0(record.key(), config.getKeyType());
		Object value = unmarshalObject0(record.value(), config.getValueType());
		return newMessageBuilder()
				.key(key)
				.value(value)
				.topic(record.topic())
				.partition(record.partition())
				.timestamp(record.timestamp())
				.headers(record.headers().toArray())
				.build();
	}
	
	protected EzyKafkaMessageBuilder newMessageBuilder() {
		return EzyKafkaMessageBuilder.messageBuilder();
	}
	
	protected Object unmarshalObject0(Object object, Class<?> type) {
		return object == null ? null : unmarshalObject(object, type);
	}
	
	protected Object unmarshalObject(Object object, Class<?> type) {
		return unmarshaller.unmarshal(object, type);
	}
}
