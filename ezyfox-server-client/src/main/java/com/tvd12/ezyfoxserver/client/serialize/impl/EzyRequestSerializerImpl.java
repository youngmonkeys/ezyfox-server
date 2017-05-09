package com.tvd12.ezyfoxserver.client.serialize.impl;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.function.EzySerializer;

import lombok.Builder;

@Builder
public class EzyRequestSerializerImpl implements EzyRequestSerializer {

	@SuppressWarnings("rawtypes")
	private static final Map<Class, EzySerializer> SERIALIZERS = defaultSerializers();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T serialize(EzyRequest request, Class<T> outType) {
		return (T) SERIALIZERS.get(outType).serialize(request);
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, EzySerializer> defaultSerializers() {
		Map<Class, EzySerializer> answer = new HashMap<>();
		answer.put(EzyArray.class, new EzySerializer<EzyRequest, EzyArray>() {
			@Override
			public EzyArray serialize(EzyRequest request) {
				return EzyEntityFactory.create(EzyArrayBuilder.class)
						.append(request.getCommand().getId())
						.append(request.getData())
						.build();
			}
		});
		return answer;
	}

}