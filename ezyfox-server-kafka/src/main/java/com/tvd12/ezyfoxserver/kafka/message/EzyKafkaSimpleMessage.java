package com.tvd12.ezyfoxserver.kafka.message;

import org.apache.kafka.common.header.Header;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyKafkaSimpleMessage implements EzyKafkaMessage {
	
	protected Object key;
	protected Object value;
	protected String topic;
	protected Long timestamp;
	protected Integer partition;
	protected Iterable<Header> headers;
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("topic: ").append(topic).append(", ")
				.append("key: ").append(key).append(", ")
				.append("value: ").append(value).append(", ")
				.append("partition: ").append(partition).append(", ")
				.append("timestamp: ").append(timestamp).append(", ")
				.append("headers: ").append(headers)
				.toString();
	}
	
}

