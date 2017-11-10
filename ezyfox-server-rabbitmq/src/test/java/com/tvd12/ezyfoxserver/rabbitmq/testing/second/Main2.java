package com.tvd12.ezyfoxserver.rabbitmq.testing.second;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.RpcClient.Response;
import com.rabbitmq.client.RpcServer;

public class Main2 {

	public static void main(String[] args) throws Exception {
		Thread serverThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread().getName() + ": start server");
					newRpcServer().mainloop();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		serverThread.start();
		System.out.println(Thread.currentThread().getName() + ": rpc server");
		
		Thread.sleep(1000L);
		
		System.out.println(Thread.currentThread().getName() + ": rpc server after sleep");
		
		RpcClient client = newRpcClient();
		
		String replyQueueName = client.getChannel().queueDeclare().getQueue();
		
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
				.replyTo(replyQueueName)
				.correlationId("1")
				.build();
		Response response = client.doCall(props, "hello-world".getBytes());
		System.out.println(new String(response.getBody()));
	}
	
	private static RpcClient newRpcClient() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare("my-exchange", "direct");
		channel.queueBind("rpc_queue", "my-exchange", "rpc_queue");
		return new RpcClient(channel, "my-exchange", "rpc_queue", "rpc_queue", 3 * 1000);
	}
	
	private static RpcServer newRpcServer() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("rpc_queue", false, false, false, null);
		channel.basicQos(1);
		return new RpcServer(channel, "rpc_queue") {
			public byte[] handleCall(byte[] requestBody, BasicProperties replyProperties) {
				return (new String(requestBody) + "#FromServer").getBytes();
			};
		};
	}
	
}
