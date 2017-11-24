package com.tvd12.ezyfoxserver.rabbitmq;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyRabbitSimpleServerConfig 
		implements EzyRabbitServerConfig, Serializable {
	private static final long serialVersionUID = 7480071532492822225L;

	protected String queue;
	protected boolean autoAck;
	protected boolean noLocal;
	protected boolean exclusive;
	protected String consumerTag;
	protected Map<String, Object> arguments;
	
}
