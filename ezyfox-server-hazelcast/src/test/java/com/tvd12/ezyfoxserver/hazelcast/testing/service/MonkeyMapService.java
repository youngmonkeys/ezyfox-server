package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import com.tvd12.ezyfoxserver.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyHazelcastMapService;

@EzyMapServiceAutoImpl("monkey")
public interface MonkeyMapService extends EzyHazelcastMapService<String, Monkey> {
}
