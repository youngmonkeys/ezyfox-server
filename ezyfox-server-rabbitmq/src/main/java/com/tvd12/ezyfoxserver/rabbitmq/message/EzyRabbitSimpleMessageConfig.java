package com.tvd12.ezyfoxserver.rabbitmq.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyRabbitSimpleMessageConfig 
		implements EzyRabbitMessageConfig, Serializable {
	private static final long serialVersionUID = -2082999913957502894L;
	
	protected Class<?> bodyType;
	
}
