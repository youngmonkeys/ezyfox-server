package com.tvd12.ezyfoxserver.rabbitmq.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRpcProcedureCaller;
import com.tvd12.ezyfoxserver.rabbitmq.endpoint.EzyRabbitConnectionFactoryBuilder;
import com.tvd12.ezyfoxserver.rabbitmq.endpoint.EzyRabbitRpcClient;
import com.tvd12.ezyfoxserver.rabbitmq.endpoint.EzyRabbitRpcServer;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcSimpleProcedure;

public class RabbitRpcAllRunner {

	public static void main(String[] args) throws Exception {
		EzyEntityFactory.create(EzyArrayBuilder.class);
		startServer();
		sleep();
//		asynRpc();
		rpc();
	}
	
	protected static void startServer() throws Exception {
		new Thread(()-> {
			try {
				System.out.println("thread-" + Thread.currentThread().getName() + ": start server");
				newServer().start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
	}
	
	protected static void sleep() throws Exception {
		Thread.sleep(1000);
	}
	
	protected static void asynRpc() {
		new Thread() {
			public void run() {
				try {
					rpc();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}
		.start();
	}
	
	protected static void rpc() throws Exception {
		EzyRabbitRpcClient client = newClient();
		client.start();
		System.out.println("thread-" + Thread.currentThread().getName() + ": start rpc");
		client.sync(newProcedure());
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < 1000 ; i++)
			client.sync(newProcedure());
//			System.out.println("i = " + i + ", result = " + client.sync(newProcedure()));
		System.out.println("elapsed = " + (System.currentTimeMillis() - start));
	}
	
	protected static EzyRpcProcedure newProcedure() {
		EzyRpcSimpleProcedure procedure = new EzyRpcSimpleProcedure();
		procedure.setParent("");
		procedure.setName("");
		procedure.setArguments(EzyEntityFactory.create(EzyArrayBuilder.class).append(100).build());
		procedure.setReturnType(int.class);
		return procedure;
	}
	
	protected static EzyRabbitRpcClient newClient() throws Exception {
		ConnectionFactory connectionFactory = new EzyRabbitConnectionFactoryBuilder()
			.build();
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.basicQos(1);
		channel.queueDeclare("rmqia-rpc-queue", false, false, false, null);
		channel.queueDeclare("rmqia-rpc-client-queue", false, false, false, null);
		channel.queueBind("rmqia-rpc-queue", "rmqia-rpc-exchange", "rmqia-rpc-routing-key");
		return EzyRabbitRpcClient.builder()
				.timeout(300 * 1000)
				.channel(channel)
				.queue("rmqia-rpc-queue")
				.exchange("rmqia-rpc-exchange")
				.routingKey("rmqia-rpc-routing-key")
				.replyQueue("rmqia-rpc-client-queue")
				.messageSerializer(newMessageSerializer())
				.messageDeserializer(newMessageDeserializer())
				.build();
	}
	
	protected static EzyRabbitRpcServer newServer() throws Exception {
		ConnectionFactory connectionFactory = new EzyRabbitConnectionFactoryBuilder()
				.build();
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			channel.basicQos(1);
			channel.queueDeclare("rmqia-rpc-queue", false, false, false, null);
		return EzyRabbitRpcServer.builder()
				.queue("rmqia-rpc-queue")
				.channel(channel)
				.messageSerializer(newMessageSerializer())
				.messageDeserializer(newMessageDeserializer())
				.procedureCaller(new EzyRpcProcedureCaller() {
					
					@Override
					public Object call(EzyRpcProcedure procedure) {
						int n = procedure.getArguments().get(0, int.class);
						return n + 3;
					}
				})
				.build();
	}
	
	protected static EzyMessageSerializer newMessageSerializer() {
		return new MsgPackSimpleSerializer();
	}
	
	protected static EzyMessageDeserializer newMessageDeserializer() {
		return new MsgPackSimpleDeserializer();
	}
	
	protected static Logger getLogger() {
		return LoggerFactory.getLogger(RabbitRpcAllRunner.class);
	}
	
}
