package com.tvd12.ezyfoxserver.rabbitmq.testing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.message.handler.EzyMessageHandler;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRabbitClient;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRabbitServer;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRabbitServerConfigBuilder;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRabbitSimpleClient;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRabbitSimpleServer;
import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessage;
import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessageBuilder;
import com.tvd12.ezyfoxserver.rabbitmq.message.EzyRabbitMessageConfigBuilder;
import com.tvd12.ezyfoxserver.rabbitmq.testing.entity.HelloMessage;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;

public class ClientServerTest {

	private EzyBindingContext bindingContext;
	
	public ClientServerTest() {
		this.bindingContext = newBindingContext();
	}
	
	public static void main(String[] args) throws Exception {
		new ClientServerTest().start();
	}
	
	private void start() throws Exception {
		EzyRabbitServer server = newServer();
		EzyRabbitClient client = newClient();
		server.start();
		EzyRabbitMessage message = EzyRabbitMessageBuilder.messageBuilder()
				.body(new HelloMessage(1))
				.build();
		client.send(message);
		System.out.println("message sent");
	}
	
	private EzyRabbitClient newClient() throws Exception {
		EzyMarshaller marshaller = bindingContext.newMarshaller();
		Channel channel = createChannel();
		EzyRabbitSimpleClient client = new EzyRabbitSimpleClient();
		client.setChannel(channel);
		client.setMarshaller(marshaller);
		client.setMessageSerializer(new MsgPackSimpleSerializer());
		return client;
	}
	
	private EzyRabbitServer newServer() throws Exception {
		EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
		Channel channel = createChannel();
		EzyRabbitSimpleServer server = new EzyRabbitSimpleServer();
		server.setChannel(channel);
		server.setUnmarshaller(unmarshaller);
		server.setMessageConfig(EzyRabbitMessageConfigBuilder.messageConfigBuilder()
				.bodyType(HelloMessage.class)
				.build());
		server.setMessageDeserializer(new MsgPackSimpleDeserializer());
		server.setServerConfig(EzyRabbitServerConfigBuilder.serverConfigBuilder()
				.queue("example-queue")
				.build());
		server.setStartService(EzyExecutors.newSingleThreadExecutor("rabbit-server"));
		server.addMessagesHandler(new EzyMessageHandler<EzyRabbitMessage>() {
			@Override
			public void handleMessage(EzyRabbitMessage message) {
				System.out.println("received message: " + message);
			}
		});
		server.addExceptionHandler(new EzyExceptionHandler() {
			
			@Override
			public void handleException(Thread thread, Throwable throwable) {
				throwable.printStackTrace();
			}
		});
		return server;
	}
	
	private EzyBindingContext newBindingContext() {
		return EzySimpleBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.rabbitmq.testing.entity")
				.build();
	}
	
	private Channel createChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("example-queue", false, false, false, null);
        return channel;
	}
	
}
