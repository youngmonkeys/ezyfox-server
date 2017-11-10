package com.tvd12.ezyfoxserver.rabbitmq.testing.third;

public class Main {

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new RPCServer().start();
			}
		}).start();
		
		RPCClient fibonacciRpc = new RPCClient();

		System.out.println(" [x] Requesting fib(30)");
		String response = fibonacciRpc.call("30");
		System.out.println(" [.] Got '" + response + "'");

		fibonacciRpc.close();
	}
	
}
