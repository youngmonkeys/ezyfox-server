package com.tvd12.ezyfoxserver.hazelcast.testing.service;

public interface DogService {

	void updateAge(String name);
	
	void updateAge2(String name);
	
	long updateAge3(String name);
	
	void throwException(String name);
	
	void throwException2(String name);
}
