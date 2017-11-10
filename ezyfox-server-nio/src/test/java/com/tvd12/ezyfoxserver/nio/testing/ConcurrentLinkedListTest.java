package com.tvd12.ezyfoxserver.nio.testing;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedListTest {

	public static void main(String[] args) {
		ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
		Thread write = new Thread(() -> {
			int i = 0;
			while(true) {
				queue.add(i ++);
			}
		});
		write.start();
		
		Thread[] reads = new Thread[200];
		for(int i = 0 ; i < reads.length ; i++) {
			final int index = i;
			reads[index] = new Thread(() -> {
				while(true) {
					System.out.println("thread-" + index + "\t\t" + queue.poll());
				}
			});
		}
		for(int i = 0 ; i < reads.length ; i++) {
			reads[i].start();
		}
	}
	
}
