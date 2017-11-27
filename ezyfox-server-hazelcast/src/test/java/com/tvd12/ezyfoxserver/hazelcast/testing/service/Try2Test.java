package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.bean.EzyServicesImplementer;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;

public class Try2Test extends HazelcastBaseTest {

	@Test
	public void test1() {
		EzyServicesImplementer implementer = EzyServicesImplementer.servicesImplementer()
				.scan(new String[] {"com.tvd12.ezyfoxserver.hazelcast.testing.service"})
				.serviceInterface("chicken", ChickenMapService.class);
		Map<Class<?>, Object> map = implementer.implement(HZ_INSTANCE);
		MonkeyMapService service = (MonkeyMapService) map.get(MonkeyMapService.class);
		service.put("dung", new Monkey("dung", 25));
		Monkey monkey = service.get("dung");
		assert monkey.getAge() == 25;
		ChickenMapService chickenMapService = (ChickenMapService)map.get(ChickenMapService.class);
		chickenMapService.put("chicken", new Chicken("chicken", 10));
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzyServicesImplementer implementer = EzyServicesImplementer.servicesImplementer()
				.scan(new String[] {"com.tvd12.ezyfoxserver.hazelcast.testing.service"});
		Map<Class<?>, Object> map = implementer.implement(HZ_INSTANCE);
		MonkeyMapService service = (MonkeyMapService) map.get(MonkeyMapService.class);
		service.put(new Monkey("dung", 25));
	}
	
}
