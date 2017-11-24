package com.tvd12.ezyfoxserver.rabbitmq;

import static com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessageBuilder.messageBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.message.handler.EzyListMessageHandlers;
import com.tvd12.ezyfoxserver.message.handler.EzyMessageHandler;
import com.tvd12.ezyfoxserver.message.handler.EzyMessageHandlers;
import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessage;
import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessageConfig;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyListExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

public class EzyRabbitSimpleServer
		extends EzyLoggable
		implements EzyRabbitServer, EzyRabbitChannelAware, Consumer, Runnable {

	@Setter
	protected Channel channel;
	@Setter
	protected ExecutorService startService;
	@Setter
	protected EzyRabbitServerConfig serverConfig;
	@Setter
	protected EzyRabbitMessageConfig messageConfig;

	@Setter
	protected EzyUnmarshaller unmarshaller;
	@Setter
	protected EzyMessageDeserializer messageDeserializer;
	
	protected EzyMessageHandlers messageHandlers = new EzyListMessageHandlers();
	protected EzyExceptionHandlers exceptionHandlers = new EzyListExceptionHandlers();
	
	@Override
	public void run() {
		try {
			start0();
		}
		catch(Exception e) {
			throw new IllegalStateException("can't start server", e);
		}
	}
	
	@Override
	public void start() throws Exception {
		if(startService == null)
			start0();
		else
			startService.execute(this);
	}
	
	protected void start0() throws Exception {
		channel.basicConsume(
				serverConfig.getQueue(), 
				serverConfig.isAutoAck(), 
				serverConfig.getConsumerTag(), 
				serverConfig.isNoLocal(), 
				serverConfig.isExclusive(), 
				serverConfig.getArguments(),
				this
		);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addMessagesHandler(EzyMessageHandler messageHandler) {
		messageHandlers.addMessageHandler(messageHandler);
	}
	
	@Override
	public void addExceptionHandler(EzyExceptionHandler exceptionHandler) {
		exceptionHandlers.addExceptionHandler(exceptionHandler);
	}
	
	@Override
	public void handleConsumeOk(String consumerTag) {
	}

	@Override
	public void handleCancelOk(String consumerTag) {
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleDelivery(String consumerTag,
            Envelope envelope,
            AMQP.BasicProperties properties,
            byte[] body) throws IOException {
		try {
			Object object = bytes2object(body);
			EzyRabbitMessage message = messageBuilder()
					.body(object)
					.exchange(envelope.getExchange())
					.routingKey(envelope.getRoutingKey())
					.properties(properties)
					.build();
			messageHandlers.handleMessage(message);
		}
		catch(Exception e) {
			exceptionHandlers.handleException(Thread.currentThread(), e);
		}
	}
	
	protected Object bytes2object(byte[] bytes) {
		Object data = messageDeserializer.deserialize(bytes);
		Object object = unmarshaller.unmarshal(data, messageConfig.getBodyType());
		return object;
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
	}
	
	@Override
	public void shutdown() {
		startService.shutdown();
	}

	
}
