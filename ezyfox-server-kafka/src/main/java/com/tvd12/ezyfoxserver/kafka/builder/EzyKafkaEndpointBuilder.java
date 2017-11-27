package com.tvd12.ezyfoxserver.kafka.builder;

import java.io.InputStream;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface EzyKafkaEndpointBuilder<B> {

	B properties(Map properties);
	
	B property(Object key, Object value);
	
	B propertiesFile(String propertiesFile);
	
	B propertiesStream(InputStream propertiesStream);
	
}
