package com.tvd12.ezyfoxserver.rabbitmq.testing.first;

public class Main {
	
	public Main() throws Exception {

		QueueConsumer consumer = new QueueConsumer("queue");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		Producer producer = new Producer("queue");

		long time = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			producer.sendMessage("String message#" + i);
			System.out.println("Message Number " + i + " sent.");
		}
		long offset = System.currentTimeMillis() - time;
		System.out.println("elapsed time = " + offset);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new Main();
	}
}
