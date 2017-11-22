package com.tvd12.ezyfoxserver.kafka.serialization;

import static com.tvd12.ezyfoxserver.kafka.constant.EzySerializationConfig.MESSAGE_DESERIALIZER;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;

public class EzySimpleDeserializer implements Deserializer<Object> {

	protected EzyMessageDeserializer deserializer;
	
	@Override
	public void close() {
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		String deserializerImplClass = (String) configs.get(MESSAGE_DESERIALIZER);
		deserializer = EzyClasses.newInstance(deserializerImplClass); 
	}
	
	@Override
	public Object deserialize(String topic, byte[] data) {
		return deserializer.deserialize(data);
	}
	
	
}
