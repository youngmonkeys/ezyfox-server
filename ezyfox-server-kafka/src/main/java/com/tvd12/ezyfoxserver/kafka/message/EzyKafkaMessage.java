package com.tvd12.ezyfoxserver.kafka.message;

import org.apache.kafka.common.header.Header;

public interface EzyKafkaMessage {

	<K> K getKey();
	
	<V> V getValue();
	
	String getTopic();
	
	Long getTimestamp();
	
	Integer getPartition();
	
	Iterable<Header> getHeaders();
	
}
