package com.tvd12.ezyfoxserver.hazelcast.testing.mapstore;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMongoMapstore;
import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;

public class ExampleMongoMapstore extends EzyMongoMapstore<String, ExampleUser> {

	@Override
	public void store(String key, ExampleUser value) {
	}

	@Override
	public ExampleUser load(String key) {
		return null;
	}
	
}
