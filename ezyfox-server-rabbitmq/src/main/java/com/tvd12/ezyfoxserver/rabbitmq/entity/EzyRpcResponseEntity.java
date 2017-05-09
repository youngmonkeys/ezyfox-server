package com.tvd12.ezyfoxserver.rabbitmq.entity;

public interface EzyRpcResponseEntity {

	<T> T getBody();
	
	EzyRpcHeaders getHeaders();
}
