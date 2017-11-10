package com.tvd12.ezyfoxserver.hazelcast;

import com.hazelcast.config.Config;

public interface EzyConfigCustomizer {
	
	void customize(Config config);
	
}
