package com.tvd12.ezyfoxserver.netty.creator;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

public interface EzyDataHandlerCreator {

	ChannelHandler newHandler(Channel channel, EzyConnectionType type);
	
}
