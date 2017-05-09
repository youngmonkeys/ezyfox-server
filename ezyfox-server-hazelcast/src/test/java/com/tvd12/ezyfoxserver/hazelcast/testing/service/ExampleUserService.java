package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;

public interface ExampleUserService {

	void saveUser(ExampleUser user);
	
	void saveUser(List<ExampleUser> users);
	
	ExampleUser getUser(String username);
	
	Map<String, ExampleUser> getUsers(Set<String> usernames);
	
	void deleteUser(String username);
	
	void deleteAllUser();
	
}
