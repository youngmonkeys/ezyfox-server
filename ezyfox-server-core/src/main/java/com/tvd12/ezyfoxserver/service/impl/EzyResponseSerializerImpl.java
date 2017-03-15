package com.tvd12.ezyfoxserver.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyFactory;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;

import lombok.Builder;

@Builder
public class EzyResponseSerializerImpl implements EzyResponseSerializer {

	@SuppressWarnings("rawtypes")
	private static final Map<Class, Serializer> SERIALIZERS;

	static {
		SERIALIZERS = defaultSerializers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T serialize(EzyResponse request, Class<T> outType) {
		return (T) SERIALIZERS.get(outType).serialize(request);
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, Serializer> defaultSerializers() {
		Map<Class, Serializer> answer = new HashMap<>();
		answer.put(EzyArray.class, new Serializer<EzyResponse, EzyArray>() {
			@Override
			public EzyArray serialize(EzyResponse request) {
				return EzyFactory.create(EzyArrayBuilder.class)
						.append(request.getCommand().getId())
						.append(request.getData())
						.build();
			}
		});
		return answer;
	}

}

interface Serializer<I,O> {
	O serialize(I request);
}
