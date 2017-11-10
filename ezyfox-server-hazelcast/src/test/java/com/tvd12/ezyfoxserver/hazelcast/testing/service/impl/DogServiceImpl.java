package com.tvd12.ezyfoxserver.hazelcast.testing.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleHazelcastMapService;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.Dog;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.DogService;

public class DogServiceImpl 
	extends EzySimpleHazelcastMapService<String, Dog>
	implements DogService {

	public DogServiceImpl(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}

	@Override
	public void updateAge(String name) {
		try {
			transactionUpdate(name, dog -> dog.setAge(100), new Dog());
		} catch (Exception e) {
		}
	}
	
	@Override
	public void updateAge2(String name) {
		try {
			transactionUpdate(name, dog -> dog.setAge(100), null);
		} catch (Exception e) {
		}
	}
	
	@Override
	public long updateAge3(String name) {
		try {
			return transactionUpdateAndGet(name, dog -> (long)dog.getAge(), null);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void throwException(String name) {
		try {
			transactionUpdate(name, dog -> dog.exception(), new Dog());
		} catch (Exception e) {
		}
	}
	
	@Override
	public void throwException2(String name) {
		try {
			transactionUpdateAndGet(name, dog -> dog.exception2(), new Dog());
		} catch (Exception e) {
		}
	}
	
	@Override
	protected String getMapName() {
		return "dog";
	}
	
}
