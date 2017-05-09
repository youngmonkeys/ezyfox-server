package com.tvd12.ezyfoxserver.hazelcast.testing.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleHazelcastMapService;
import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfoxserver.hazelcast.testing.service.ExampleUserService;

public class ExampleUserServiceImpl
		extends EzySimpleHazelcastMapService<String, ExampleUser>
		implements ExampleUserService {

	@Override
	public void saveUser(ExampleUser user) {
		map.set(user.getUsername(), user);
	}
	
	@Override
	public void saveUser(List<ExampleUser> users) {
		set(users);
		set(new HashMap<>());
	}
	
	@Override
	public ExampleUser getUser(String username) {
		return map.get(username);
	}
	
	@Override
	protected String getMapName() {
		return "example_users";
	}

	@Override
	public Map<String, ExampleUser> getUsers(Set<String> usernames) {
		return getMapByIds(usernames);
	}

	@Override
	public void deleteUser(String username) {
		remove(username);
	}

	@Override
	public void deleteAllUser() {
		clear();
	}
	
}
