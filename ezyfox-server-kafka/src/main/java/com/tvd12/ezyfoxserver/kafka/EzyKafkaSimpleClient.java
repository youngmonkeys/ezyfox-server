package com.tvd12.ezyfoxserver.kafka;

import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;
import com.tvd12.ezyfoxserver.kafka.record.EzyKafkaProducerRecordCreator;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzyKafkaSimpleClient 
		extends EzyLoggable 
		implements EzyKafkaClient, EzyKafkaProducerAware {

	protected Producer producer;
	protected EzyKafkaProducerRecordCreator recordCreator;
	
	@Override
	public void flush() {
		producer.flush();
	}
	
	@Override
	public void shutdown() {
		producer.close();
	}
	
	@Override
	public Future<?> send(EzyKafkaMessage message) {
		ProducerRecord record = createRecord(message);
		return producer.send(record);
	}
	
	@Override
	public RecordMetadata sync(EzyKafkaMessage message) {
		try {
			Object answer = send(message).get();
			return (RecordMetadata)answer;
		} catch (Exception e) {
			throw new IllegalArgumentException("can't send message: " + message, e);
		}
	}
	
	@Override
	public void async(EzyKafkaMessage message, Callback callback) {
		ProducerRecord record = createRecord(message);
		producer.send(record, callback);
	}
	
	protected ProducerRecord createRecord(EzyKafkaMessage message) {
		return recordCreator.create(message);
	}
	
}
