package com.tvd12.ezyfoxserver.hazelcast.testing.mapstore;

import java.util.Map;

import com.tvd12.ezyfoxserver.database.annotation.EzyMapstore;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyAbstractMapstore;
import com.tvd12.ezyfoxserver.hazelcast.testing.constant.Entities;
import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfoxserver.io.EzyMaps;

@EzyMapstore(Entities.USER)
public class ExampleUserMapstore extends EzyAbstractMapstore<String, ExampleUser> {

	private static final Map<String, ExampleUser> MAP = 
			EzyMaps.newHashMap("dungtv", new ExampleUser("dungtv"));
	
	@Override
	public void store(String key, ExampleUser value) {
		MAP.put(key, value);
	}

	@Override
	public ExampleUser load(String key) {
		return MAP.get(key);
	}

	
	
}
