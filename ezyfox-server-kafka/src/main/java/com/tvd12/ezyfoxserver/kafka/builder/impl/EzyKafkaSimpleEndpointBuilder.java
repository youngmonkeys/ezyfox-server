package com.tvd12.ezyfoxserver.kafka.builder.impl;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaEndpointBuilder;
import com.tvd12.ezyfoxserver.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.properties.file.reader.BaseFileReader;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class EzyKafkaSimpleEndpointBuilder<B>
		extends EzyLoggable
		implements EzyKafkaEndpointBuilder<B> {

	protected Properties properties = new Properties();
	
	@Override
	public B properties(Map properties) {
		this.properties.putAll(properties);
		return (B)this;
	}

	@Override
	public B property(Object key, Object value) {
		this.properties.put(key, value);
		return (B)this;
	}

	@Override
	public B propertiesFile(String propertiesFile) {
		InputStream inputStream = EzyAnywayInputStreamLoader.builder()
				.build()
				.load(propertiesFile);
		return propertiesStream(inputStream);
	}
	
	@Override
	public B propertiesStream(InputStream propertiesStream) {
		Properties properties = readPropertiesStream(propertiesStream);
		return properties(properties);
	}

	private Properties readPropertiesStream(InputStream propertiesStream) {
		return new BaseFileReader().loadInputStream(propertiesStream);
	}
}
