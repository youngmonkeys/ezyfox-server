package com.tvd12.ezyfoxserver.kafka.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.common.header.Header;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzyKafkaMessageBuilder implements EzyBuilder<EzyKafkaMessage> {

	protected Object key;
	protected Object value;
	protected String topic;
	protected Long timestamp;
	protected Integer partition;
	protected List<Header> headers = new ArrayList<>();
	
	public static EzyKafkaMessageBuilder messageBuilder() {
		return new EzyKafkaMessageBuilder();
	}
	
	public EzyKafkaMessageBuilder key(Object key) {
		this.key = key;
		return this;
	}
	
	public EzyKafkaMessageBuilder value(Object value) {
		this.value = value;
		return this;
	}
	
	public EzyKafkaMessageBuilder topic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public EzyKafkaMessageBuilder timestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	public EzyKafkaMessageBuilder partition(Integer partition) {
		this.partition = partition;
		return this;
	}
	
	public EzyKafkaMessageBuilder header(Header header) {
		this.headers.add(header);
		return this;
	}
	
	public EzyKafkaMessageBuilder headers(Header... headers) {
		return headers(Arrays.asList(headers));
	}
	
	public EzyKafkaMessageBuilder headers(Iterable<Header> headers) {
		headers.forEach(this::header);
		return this;
	}
	
	@Override
	public EzyKafkaMessage build() {
		EzyKafkaSimpleMessage message = new EzyKafkaSimpleMessage();
		message.setKey(key);
		message.setValue(value);
		message.setTopic(topic);
		message.setHeaders(headers);
		message.setTimestamp(timestamp);
		message.setPartition(partition);
		return message;
	}
}
