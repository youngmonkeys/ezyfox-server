package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.service.EzyAbstractHazelcastService;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;

public class EzyAbstractHazelcastServiceTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		MyService service = new MyService() {};
		service.setHazelcastInstance(HZ_INSTANCE);
		service.init();
		service.one();
		service.two();
		service.three();
		service.four();
		service.five();
	}
	
	public static class MyService extends EzyAbstractHazelcastService {
		
		public void one() throws Exception {
			lockUpdate("1", () -> {});
			lockUpdateAndGet("2", () -> 1L);
			lockUpdateWithException("3", () -> {});
			lockUpdateAndGetWithException("4", () -> 1L);
		}
		
		public void two() {
			try {
				lockUpdate("1", () -> {throw new Exception();});
			}
			catch(Exception e) {
			}
		}
		
		public void three() {
			try {
				lockUpdateAndGet("1", () -> {throw new Exception();});
			}
			catch(Exception e) {
			}
		}
		
		public void four() {
			try {
				lockUpdateWithException("1", () -> {throw new Exception();});
			}
			catch(Exception e) {
			}
		}
		
		public void five() {
			try {
				lockUpdateAndGetWithException("1", () -> {throw new Exception();});
			}
			catch(Exception e) {
			}
		}
		
	}
	
}
