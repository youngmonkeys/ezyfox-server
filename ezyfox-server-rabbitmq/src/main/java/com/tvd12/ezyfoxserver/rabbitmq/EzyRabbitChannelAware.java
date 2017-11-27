package com.tvd12.ezyfoxserver.rabbitmq;

import com.rabbitmq.client.Channel;

public interface EzyRabbitChannelAware {

	void setChannel(Channel channel);
	
}
