package com.tvd12.ezyfoxserver.rabbitmq.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString(of = {"headers", "body"})
public class EzyRpcSimpleResponseEntity implements EzyRpcResponseEntity {
	
	protected Object body;
	protected EzyRpcHeaders headers; 
	
}
