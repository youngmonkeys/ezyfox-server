package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.impl.DogServiceImpl;

public class DogServiceTest extends HazelcastBaseTest {

	@Test
	public void test1() {
		DogServiceImpl service = new DogServiceImpl(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.updateAge("hehe");
	}
	
	@Test
	public void test2() {
		DogServiceImpl service = new DogServiceImpl(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.updateAge2("hoho");
	}
	
	@Test
	public void test3() {
		DogServiceImpl service = new DogServiceImpl(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.updateAge3("hoho");
	}
	
	@Test
	public void test4() {
		DogServiceImpl service = new DogServiceImpl(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.throwException("hehe");
	}
	
	@Test
	public void test5() {
		DogServiceImpl service = new DogServiceImpl(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.throwException2("hehe");
	}
	
}
