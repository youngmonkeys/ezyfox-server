package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.hazelcast.impl.EzySimpleServiceImplementer;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleHazelcastMapService;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

public class TryTest extends HazelcastBaseTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		EzySimpleServiceImplementer implementer =
				new EzySimpleServiceImplementer(new EzyClass(PersonMapService.class));
		PersonMapService service = (PersonMapService)implementer.implement(HZ_INSTANCE);
		service.clear();
		assert service.isEmpty();
		service.put(new Person("dung", 25));
		Person person = service.get("dung");
		assert person.getAge() == 25;
		service.put(new Person("duong", 26));
		assert service.getAllList().size() == 2;
		assert service.getAllMap().size() == 2;
		assert service.getListByIds(Sets.newHashSet("dung", "duong")).size() == 2;
		assert service.getMapByIds(Sets.newHashSet("dung", "duong")).size() == 2;
		service.set(new Person("dat", 27));
		service.set("son", new Person("son", 28));
		assert service.size() == 4;
		service.remove(Sets.newHashSet("dat", "son"));
		assert service.size() == 2;
		assert service.containsKey("dung");
		assert !service.containsKey("son");
		assert service.containsValue(person);
		assert !service.containsValue(new Person("not found", 1));
		service.put("hung", new Person("hung", 32));
		assert !service.isEmpty();
		EzySimpleHazelcastMapService<String, Person> ab = (EzySimpleHazelcastMapService<String, Person>)service;
		assert ab.getListByField("name", "dung").size() == 1;
	}
	
}
