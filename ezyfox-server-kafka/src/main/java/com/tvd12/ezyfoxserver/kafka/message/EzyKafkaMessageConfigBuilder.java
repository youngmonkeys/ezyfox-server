package com.tvd12.ezyfoxserver.kafka.message;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

public class EzyKafkaMessageConfigBuilder implements EzyBuilder<EzyKafkaMessageConfig> {

	protected Class<?> keyType;
	protected Class<?> valueType;
	
	public static EzyKafkaMessageConfigBuilder messageConfigBuilder() {
		return new EzyKafkaMessageConfigBuilder();
	}
	
	public EzyKafkaMessageConfigBuilder keyType(Class<?> keyType) {
		this.keyType = keyType;
		return this;
	}
	
	public EzyKafkaMessageConfigBuilder valueType(Class<?> valueType) {
		this.valueType = valueType;
		return this;
	}
	
	@Override
	public EzyKafkaMessageConfig build() {
		EzyKafkaSimpleMessageConfig config = new EzyKafkaSimpleMessageConfig();
		config.setKeyType(keyType);
		config.setValueType(valueType);
		return config;
	}
	
}
