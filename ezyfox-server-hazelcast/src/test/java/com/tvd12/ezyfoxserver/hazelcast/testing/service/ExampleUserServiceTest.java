package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.impl.ExampleUserServiceImpl;

public class ExampleUserServiceTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		ExampleUserServiceImpl service = new ExampleUserServiceImpl();
		service.setHazelcastInstance(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		assert service.get("dungtv") != null;
		assert service.get("abc") == null;
		assert service.getMapByIds(Sets.newHashSet("dungtv", "abc")).size() == 1;
		
		service.saveUser(new ExampleUser("dungtv1"));
		service.saveUser(Lists.newArrayList(new ExampleUser("dungtv1"), new ExampleUser("dungtv2")));
		
		service.deleteUser("dungtv1");
		service.clear();
		Thread.sleep(3000L);
	}
	
}
