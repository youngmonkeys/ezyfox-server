package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyHazelcastMapService;

@EzyAutoImpl(properties = {
		@EzyKeyValue(key = "map.name", value = "person")
})
public interface PersonMapService extends EzyHazelcastMapService<String, Person> {
}
