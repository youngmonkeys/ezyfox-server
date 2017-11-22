package com.tvd12.ezyfoxserver.kafka.record;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyMarshallerAware;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetcher;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetchers;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
public class EzyKafkaSimpleProducerRecordCreator
		extends EzyLoggable
		implements EzyKafkaProducerRecordCreator, EzyMarshallerAware {

	protected EzyMarshaller marshaller;
	protected EzyIdFetchers messageIdFetchers;
	
	@SuppressWarnings("rawtypes")
	public ProducerRecord create(EzyKafkaMessage message) {
		Object value = marshalValue(message.getValue());
		Object key = marshalKey(message.getKey(), message.getValue());
		return new ProducerRecord<>
		(
				message.getTopic(), 
				message.getPartition(), 
				message.getTimestamp(), 
				key, 
				value, 
				message.getHeaders()
		);
	}
	
	protected Object marshalValue(Object object) {
		return marshalObject(object);
	}
	
	protected Object marshalObject(Object object) {
		return marshaller.marshal(object);
	}
	
	protected Object marshalKey(Object key, Object value) {
		if(key != null)
			return marshalObject(key);
		EzyIdFetcher fetcher = messageIdFetchers.getIdFetcher(value.getClass());
		return fetcher.getId(value);
	}
	
}
