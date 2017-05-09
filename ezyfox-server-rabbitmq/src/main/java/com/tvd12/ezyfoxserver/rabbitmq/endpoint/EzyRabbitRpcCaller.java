package com.tvd12.ezyfoxserver.rabbitmq.endpoint;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcClient;

public class EzyRabbitRpcCaller extends RpcClient {

	public EzyRabbitRpcCaller(Channel channel, String exchange, String routingKey, String replyTo, int timeout) throws
            IOException {
        super(channel, exchange, routingKey, replyTo, timeout);
    }
 
}
