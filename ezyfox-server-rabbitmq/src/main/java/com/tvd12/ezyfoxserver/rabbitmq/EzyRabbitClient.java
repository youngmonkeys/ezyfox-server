package com.tvd12.ezyfoxserver.rabbitmq;

import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessage;
import com.tvd12.ezyfoxserver.util.EzyShutdownable;

public interface EzyRabbitClient extends EzyShutdownable {
	
	void send(EzyRabbitMessage message);
	
}
