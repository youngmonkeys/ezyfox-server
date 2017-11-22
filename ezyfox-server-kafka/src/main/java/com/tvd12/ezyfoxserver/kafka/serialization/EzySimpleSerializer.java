package com.tvd12.ezyfoxserver.kafka.serialization;

import static com.tvd12.ezyfoxserver.kafka.constant.EzySerializationConfig.MESSAGE_SERIALIZER;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;

public class EzySimpleSerializer implements Serializer<Object> {

	protected EzyMessageSerializer serializer;
	
	@Override
	public void close() {
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		String serializerImplClass = (String) configs.get(MESSAGE_SERIALIZER);
		serializer = EzyClasses.newInstance(serializerImplClass); 
	}

	@Override
	public byte[] serialize(String topic, Object object) {
		return serializer.serialize(object);
	}

	
	
}
