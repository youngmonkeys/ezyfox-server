package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.hazelcast.core.IMap;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleMaxIdService;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.testing.constant.Entities;
import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;

public class ExampleUserTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		EzySimpleMaxIdService service = new EzySimpleMaxIdService();
		service.setHazelcastInstance(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		long userId1 = service.incrementAndGet(Entities.USER);
		long userId2 = service.incrementAndGet(Entities.USER);
		
		ExampleUser user1 = new ExampleUser(userId1, "user1");
		ExampleUser user2 = new ExampleUser(userId2, "user2");
		
		IMap<String, ExampleUser> userMap = HZ_INSTANCE.getMap(Entities.USER);
		userMap.set("user1", user1);
		userMap.set("user2", user2);
		
		Thread.sleep(3000);
	}
	
}
